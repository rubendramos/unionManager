package controllers;

import java.lang.reflect.Constructor;
import java.util.List;
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
public class PrestamoFondos extends CRUD {

    public static void facerPrestamo(String afiliados, String efId, int page, String where, String search) {
        EntradaFondo ef = EntradaFondo.findById(Long.parseLong(efId));
        Afiliado afiliado = Afiliado.findById(Long.parseLong(afiliados));
        
        if(validaCondicionsPrestamo(ef,afiliado)){
        PrestamoFondo pf = new PrestamoFondo(afiliado, ef, Tools.getCurrentDate(), null);

        ef.setnExemplaresPrestados(Integer.toString(Integer.parseInt(ef.getnExemplaresPrestados()) + 1));
        ef._save();
        pf.setOrganismo(Seguridade.organismo());
        pf._save();
        flash.success(play.i18n.Messages.get("crud.avisoGardado", pf.toString()));
        }
        //listaPrestables(page, search, "true", null, null, null);
        EntradaFondos.listaPrestables(page, getWhereListaPrestables(), search, "true", null, null, null);
    }

    public static void facerDevolucionPrestamo(String id, String page, String where, String search) {
        try {


            PrestamoFondo pf = PrestamoFondo.findById(Long.parseLong(id));
            EntradaFondo ef = pf.entradaFondo;

            ef.setnExemplaresPrestados(Integer.toString(Integer.parseInt(ef.getnExemplaresPrestados()) - 1));

            ef._save();
            pf.setDataDevolucion(Tools.getCurrentDate());
            pf.setOrganismo(Seguridade.organismo());
            pf._save();
            flash.success(play.i18n.Messages.get("crud.avisoGardado", pf.toString()));
            ObjectType type = ObjectType.get(getControllerClass());
            if (where == null || "".equals(where)) {
                where = type.createWhereFilterClausule();
            }
            list(Integer.parseInt(page), where, search, "true", null, null, null);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PrestamoFondos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void seleccionaAfiliado(String id, String page,String search,String order,String orderBy, String fondoFiltro,String generoFiltro,String tipoEntradaFiltro) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);        
        EntradaFondo ef = EntradaFondo.findById(Long.parseLong(id));
        PrestamoFondo object = new PrestamoFondo(null, ef, null, null);
        //Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        //Model object = (Model) constructor.newInstance();
        //notFoundIfNull(object);
        List<Afiliado> afiliados = Afiliado.getListaAfiliadosAlta();
        render(afiliados, ef, page, search,fondoFiltro,generoFiltro,tipoEntradaFiltro,order,orderBy);
    }

    public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) throws Exception {

        if(orderBy==null || "".equals(orderBy)){
            order="DESC";
            orderBy="dataPrestamo";
            
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
    
    public static void listaPrestables(int page, String search, String from, String searchFields,
            String orderBy, String order){                 
            EntradaFondos.listaPrestables(page, getWhereListaPrestables(), search, from, searchFields, orderBy, order);
    }
    
    private static String getWhereListaPrestables() {
        try {
            ObjectType type = ObjectType.forClass("models.EntradaFondo");
            return type.createWhereFilterClausule();
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrestamoFondos.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private static boolean validaCondicionsPrestamo(EntradaFondo entradaFondo,Afiliado afiliado){
        boolean res=true;        
        
        List<EntradaFondo> prestamosActivosNoFondo=PrestamoFondo.getPrestamosActivosPorAfiliadoEFondo(afiliado,entradaFondo.fondo);
        List<EntradaFondo> prestamosVencidos=PrestamoFondo.getPrestamosVencidosPorAfiliado(afiliado);
        
        //Validamos se ten prestamos caducatos
        if(prestamosVencidos!=null && prestamosVencidos.size()>0){
            flash.error(play.i18n.Messages.get("prestamo.tenPrestamosVencidos"));
            res=false;
        //Validamos si supera o maximo de prestamos para o fondo
        }else if(prestamosActivosNoFondo!=null && prestamosActivosNoFondo.size()>=Integer.parseInt(entradaFondo.fondo.umaxPrestamo)){
            flash.error(play.i18n.Messages.get("prestamo.superaMaxPrestamosFondo", entradaFondo.fondo.umaxPrestamo ,entradaFondo.fondo.toString()));            
            res=false;
        //Valiadmos si xa ten o mesmo fondo prestado
        }else if (prestamosActivosNoFondo.contains(entradaFondo)){
            flash.error(play.i18n.Messages.get("prestamo.xaTenPrestado", entradaFondo.toString()));            
            res=false;
        }
        return res;
    }

}
