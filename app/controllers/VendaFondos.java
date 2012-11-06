package controllers;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import models.*;
import org.hibernate.Session;
import play.*;
import play.db.Model;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import utils.Tools;

@Check("propaganda")
@With(Secure.class)
public class VendaFondos extends CRUD {

    public static void facerVenda(String afiliados, String efId, int page, String where, String search) {
        EntradaFondo ef = EntradaFondo.findById(Long.parseLong(efId));
        Afiliado afiliado = Afiliado.findById(Long.parseLong(afiliados));
        User user = User.find("byUsuario", Seguridade.connected()).first();

        int iUnidades = Integer.parseInt(params.get("object.unidades"));
        String unidades=params.get("object.unidades");
        Double importeVenda = ef.importe * iUnidades;

        if (validaCondicionsVenda(ef, afiliado)) {
            VendaFondo vf = new VendaFondo(afiliado, ef, Tools.getCurrentDate(), importeVenda, unidades, null);

            ef.setnExemplaresVendidos(Integer.toString(Integer.parseInt(ef.getnExemplaresVendidos()) + iUnidades));
            ef._save();
            vf.setOrganismo(Seguridade.organismo());
            vf._save();
            engadeApuntamentoCuotaAFollaFondo(user, vf);
            flash.success(play.i18n.Messages.get("crud.avisoGardado", vf.toString()));
        }
        //listaPrestables(page, search, "true", null, null, null);
        EntradaFondos.listaPrestables(page, getWhereListaPrestables(), search, "true", null, null, null);
    }

    public static void facerDevolucionVenda(String id, String page, String where, String search) {
        try {


            VendaFondo vf = VendaFondo.findById(Long.parseLong(id));
            EntradaFondo ef = vf.entradaFondo;
            User user = User.find("byUsuario", Seguridade.connected()).first();            

            int iUnidades=Integer.parseInt(vf.nUnidades);
            
            ef.setnExemplaresVendidos(Integer.toString(Integer.parseInt(ef.getnExemplaresVendidos()) - iUnidades));

            ef._save();
            vf.setDataDevolucionVenda(Tools.getCurrentDate());
            vf.setOrganismo(Seguridade.organismo());
            vf._save();
            engadeApuntamentoCuotaAFollaFondo(user, vf);
            flash.success(play.i18n.Messages.get("crud.avisoGardado", vf.toString()));
            ObjectType type = ObjectType.get(getControllerClass());
            if (where == null || "".equals(where)) {
                where = type.createWhereFilterClausule();
            }
            list(Integer.parseInt(page), where, search, "true", null, null, null);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(VendaFondos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void seleccionaAfiliadoEUnidades(String id, String page, String search, String order, String orderBy, String fondoFiltro, String generoFiltro, String tipoEntradaFiltro) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        EntradaFondo ef = EntradaFondo.findById(Long.parseLong(id));
        VendaFondo object = new VendaFondo(null, ef, null, null, null, null);
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

    private static boolean validaCondicionsVenda(EntradaFondo entradaFondo, Afiliado afiliado) {
        boolean res = true;

        return res;
    }
}
