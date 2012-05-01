package controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import models.NotificacionInterna;
import models.NotificacionInterna_User;
import models.User;
import play.*;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@Check("autenticado")
@With(Secure.class)
public class NotificacionInternas extends CRUD { 
    
 
 public static void list(int page, String search, String searchFields,
            String orderBy, String order) {
        String where="";
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }

        where=type.createWhereFilterClausule();
        
        //Filtramos sempre polo id do usuario
        User u=User.find("byUsuario", Seguridade.connected()).<User>first();
        if (where == null || "".equals(where)) {
            where = " user_id=" + u.id;
        } else {
            where = where + " and  user_id=" + u.id;
        }
        
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
        Long count = type.count(search, searchFields, where);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }
 
  public static void listRecived(int page, String search, String searchFields,
            String orderBy, String order) {
        ObjectType type = ObjectType.get(getControllerClass());
        String where=type.createWhereFilterClausule();         
        User u=User.find("byUsuario", Seguridade.connected()).<User>first();
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
        
//       List<NotificacionInterna> notifications=NotificacionInterna.findByContactoOld(u.id);
//        
//        if(notifications!=null && !notifications.isEmpty()){
//                
//        for(NotificacionInterna notificacions : notifications){            
//            sNotificacions=sNotificacions +notificacions.id+",";
//        }
//        sNotificacions=sNotificacions.substring(0,sNotificacions.length()-1);
//            //Filtramos sempre polo id do usuario
//
//            if (where == null || "".equals(where)) {
//                where = " id in (" + sNotificacions +")";
//            } else {
//                where = where + "and  id in (" +sNotificacions+")";
//            }
//              List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
//        Long count = type.count(search, searchFields, where);
//        Long totalCount = type.count(null, null, (String) request.args.get("where"));
//        try {
//            render(type, objects, count, totalCount, page, orderBy, order);
//        } catch (TemplateNotFoundException e) {
//            render("CRUD/listRecived.html", type, objects, count, totalCount, page,
//                    orderBy, order);
//        }
//        }else{
//            Privado.index();
//        }

         if (where == null || "".equals(where)) {
                where = "id in (select niu.notificacionInterna_id from NotificacionInterna_User niu where niu.contactos_id=" + u.id+")";
            } else {
                where = where + "id in (select niu.notificacionInterna_id from NotificacionInterna_User niu where niu.contactos_id=" + u.id+")";
            }
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
        Long count = type.count(search, searchFields, where);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            //render(type, objects, count, totalCount, page, orderBy, order);
             render("NotificacionInternas/listRecived.html", type, objects, count, totalCount, page,
                    orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("NotificacionInternas/listRecived.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
        
       
    }
  
  
  
  
    
}