package controllers;

import java.util.List;
import java.util.logging.Level;
import models.Afiliado;
import models.EntradaFondo;
import models.PrestamoFondo;
import models.User;
import org.hibernate.Filter;
import org.hibernate.Session;
import play.*;
import play.db.Model;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@Check("propaganda")
@With(Secure.class)
public class EntradaFondos extends CRUD {

    
    
    public static void listaPrestables(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) {

        ((Session) JPA.em().getDelegate()).enableFilter("fondoPublico");      

        if("".equals(orderBy)){
            order=null;
            orderBy=null;
            
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

        where=whereClausule;
        ((Session) JPA.em().getDelegate()).disableFilter("fondoPublico"); 

        try {
            render(type, objects, count, totalCount, page, orderBy, order,where);

        } catch (TemplateNotFoundException e) {
            render("EntradaFondos/listaBusquedaEntradasFondos.html", type, objects, count, totalCount, page, orderBy, order,where);
        }
    }
    
    public static void seleccionaAfiliado(String id,String page,String where,String search,String order,String orderBy) throws Exception{
        String fondoFiltro=params.get("object.fondo.id");
        String generoFiltro=params.get("object.tipoGeneroFondo.id");
        String tipoEntradaFiltro=params.get("object.tipoEntradaFondo.id");
        //String order=params.get("order");
        //String orderBy=params.get("orderBy");
       
                                        
        PrestamoFondos.seleccionaAfiliado(id, page, search,order,orderBy,fondoFiltro,generoFiltro,tipoEntradaFiltro);
    }    
}