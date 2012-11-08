package controllers;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import models.*;
import org.hibernate.Session;
import play.*;
import play.data.validation.Validation;
import play.data.validation.ValidationPlugin;
import play.db.Model;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import utils.Tools;

@Check("propaganda")
@With(Secure.class)
public class VendaFondos extends CRUD {

    public static void facerVenda(String afiliados,int page) {
        
        String tipoEntradaFiltro=params.get("object.tipoEntradaFondo.id");
        String generoFiltro=params.get("object.generoFiltro.id");
        String fondoFiltro=params.get("object.fondoFiltro.id");
        String orderBy=params.get("object.orderBy");
        String order=params.get("object.order");        
        String search=params.get("search");
        String efId=params.get("efId");
        
        
        
        EntradaFondo ef = EntradaFondo.findById(Long.parseLong(efId));
        Afiliado afiliado=null;
        

        
        if(afiliados!=null && "".equals(afiliados)){
            afiliado = Afiliado.findById(Long.parseLong(afiliados));
        }
        
        User user = User.find("byUsuario", Seguridade.connected()).first();
        Double descontoAfiliado = 0.0;

        String unidades = params.get("object.nUnidades");

        if (validaCondicionsVenda(ef, unidades)) {
            
            int iUnidades = Integer.parseInt(unidades);
            if (afiliado != null) {
                descontoAfiliado = (ef.importe * ef.descontoAfiliados) / 100;
            }

            Double importeVenda = (ef.importe - descontoAfiliado) * iUnidades;
            VendaFondo vf = new VendaFondo(afiliado, ef, Tools.getCurrentDate(), importeVenda, iUnidades, null);

            ef.setnExemplaresVendidos(ef.getnExemplaresVendidos() + iUnidades);
            ef._save();
            vf.setOrganismo(Seguridade.organismo());
            vf._save();
            engadeApuntamentoCuotaAFollaFondo(user, vf);
            flash.success(play.i18n.Messages.get("vendaFondo.vendaFondoExito", vf.entradaFondo));
            EntradaFondos.listaEnVenda(page, getWhereListaPrestables(), search, "true", null, null, null);
        } else {
            VendaFondos.seleccionaAfiliadoEUnidades(efId, page, search, order, orderBy, fondoFiltro, generoFiltro, tipoEntradaFiltro);
        }
        //listaPrestables(page, search, "true", null, null, null);

    }

    public static void facerDevolucionVenda(String id, String page, String where, String search) {
        try {


            VendaFondo vf = VendaFondo.findById(Long.parseLong(id));
            EntradaFondo ef = vf.entradaFondo;
            User user = User.find("byUsuario", Seguridade.connected()).first();

            int iUnidades = vf.nUnidades;

            ef.setnExemplaresVendidos(ef.getnExemplaresVendidos() - iUnidades);

            ef._save();
            vf.setDataDevolucionVenda(Tools.getCurrentDate());
            vf.setOrganismo(Seguridade.organismo());
            vf._save();
            engadeApuntamentoCuotaAFollaFondo(user, vf);
            flash.success(play.i18n.Messages.get("vendaFondo.devolucionExito", vf.entradaFondo,Tools.getCurrency(vf.importeVenda)));
            ObjectType type = ObjectType.get(getControllerClass());
            if (where == null || "".equals(where)) {
                where = type.createWhereFilterClausule();
            }
            list(Integer.parseInt(page), where, search, "true", null, null, null);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(VendaFondos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void seleccionaAfiliadoEUnidades(String id, int page, String search, String order, String orderBy, String fondoFiltro, String generoFiltro, String tipoEntradaFiltro) {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        EntradaFondo ef = EntradaFondo.findById(Long.parseLong(id));
        VendaFondo object = new VendaFondo(null, ef, null, null, 0, null);
        //Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        //Model object = (Model) constructor.newInstance();
        //notFoundIfNull(object);
        List<Afiliado> afiliados = Afiliado.getListaAfiliadosAlta();
        render(afiliados, ef, page, search, fondoFiltro, generoFiltro, tipoEntradaFiltro, order, orderBy);
    }

    public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) throws Exception {

        if (orderBy == null || "".equals(orderBy)) {
            order = "DESC";
            orderBy = "dataVenda";

        }


        ObjectType type = ObjectType.get(getControllerClass());
        String whereClausule = null;

        notFoundIfNull(type);

        if (page < 1) {
            page = 1;
        }

        if (from != null && "true".equals(from)) {
            if (where != null && !"".equals(where)) {
                whereClausule = where;
            }
            type.setValuesFromSearch(where);

        } else {
            whereClausule = type.createWhereFilterClausule();
        }

        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);
        Long count = type.count(search, searchFields, whereClausule);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));



        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }

    public static void listaEnVenda(int page, String search, String from, String searchFields,
            String orderBy, String order) {
        EntradaFondos.listaEnVenda(page, getWhereListaPrestables(), search, from, searchFields, orderBy, order);
    }

    private static String getWhereListaPrestables() {
        try {
            ObjectType type = ObjectType.forClass("models.EntradaFondo");
            return type.createWhereFilterClausule();
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendaFondos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static void engadeApuntamentoCuotaAFollaFondo(User u, VendaFondo vendaFondo) {
        Organismo organismo = Seguridade.organismo();
        FollaConta follaConta = FollaConta.getFollaContasPropaganda(organismo, vendaFondo.entradaFondo.fondo);
        Apuntamento ap = Apuntamento.createApuntamentoVendaFondos(u, vendaFondo, organismo);
        Set<Apuntamento> apuntamentos = follaConta.apuntamentos;
        apuntamentos.add(ap);
        follaConta._save();

    }

    private static boolean validaCondicionsVenda(EntradaFondo entradaFondo, String unidades) {
        boolean res = true;
        
        if(unidades==null || "".equals(unidades)){
            flash.error(play.i18n.Messages.get("venda.unidadesObligatorio"));
            return false;
        }

        if(!formatoNumericoValido(unidades,2)){
            flash.error(play.i18n.Messages.get("venda.unidadesFormatoIncorrecto"));
            return false;
        }        
        
             
        if (entradaFondo.estaDisponibleParaVenda()) {          
            int iUnidades=Integer.parseInt(unidades);
            int iDisponible = Integer.parseInt(entradaFondo.exemplaresDisponibleVenda());
            if (iDisponible < iUnidades) {
                res = false;
                flash.error(play.i18n.Messages.get("venda.superaMaxDiponibleVenda", unidades, Integer.toString(iDisponible)));
            }
        } else {
            flash.error(play.i18n.Messages.get("venda.nonDisponibleVenda"));
            res = false;
        }
        return res;
    }
    
    private static boolean formatoNumericoValido(String numero,int tamMax){
        int maxValue=1;
        
        try{
            
        int iNumero=Integer.parseInt(numero);
        for(int i=0;i<tamMax;i++){
            maxValue=maxValue*10;
        }
        return (iNumero>0 && iNumero<maxValue)?true:false;
                
        }catch(NumberFormatException e){
            return false;
        }
    }    
}
