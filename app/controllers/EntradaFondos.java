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


        ((Session) JPA.em().getDelegate()).disableFilter("fondoPublico"); 

        try {
            render(type, objects, count, totalCount, page, orderBy, order);

        } catch (TemplateNotFoundException e) {
            render("EntradaFondos/listaBusquedaEntradasFondos.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }
    
    public static void seleccionaAfiliado(String id,String page,String where,String search) throws Exception{
        PrestamoFondos.seleccionaAfiliado(id, page, where, search);
    }    
}