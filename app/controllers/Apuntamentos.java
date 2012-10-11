package controllers;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import javax.mail.Session;
import models.*;
import play.*;
import play.data.binding.Binder;
import play.db.Model;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@Check("tesoureria")
@With(Secure.class)
public class Apuntamentos extends CRUD {

    public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        User u = User.find("byUsuario", Seguridade.connected()).<User>first();


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

        boolean permisoPermanencia = u.tenPermiso("Permanencia");
        boolean permisoTesoureria = u.tenPermiso("Tesoureria");

        if (permisoPermanencia && !permisoTesoureria) {

            if (whereClausule == null) {
                whereClausule = " usuarioApuntamento_id='" + u.id + "'";
            } else {
                whereClausule = whereClausule + " and usuarioApuntamento_id='" + u.id + "'";
            }
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

  
}
