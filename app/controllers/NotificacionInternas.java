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
        orderBy="dataAlta";
        order="DESC";
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }

        where=type.createWhereFilterClausule();
        
        //Filtramos sempre polo id do usuario
        User u=User.find("byUsuario", Seguridade.connected()).<User>first();
        if (where == null || "".equals(where)) {
            where = " avisode_id=" + u.id;
        } else {
            where = where + " and  avisode_id=" + u.id;
        }
        
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
        Long count = type.count(search, searchFields, where);
        Long totalCount = type.count(null, null, where);
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("NotificacionInternas/listSold.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }
 
  public static void listRecived(int page, String search, String searchFields,
            String orderBy, String order) {
        ObjectType type = ObjectType.get(getControllerClass());
        String where=type.createWhereFilterClausule();         
        User u=User.find("byUsuario", Seguridade.connected()).<User>first();
        orderBy="dataAlta";
        order="DESC";
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
        

         if (where == null || "".equals(where)) {
                where = "id in (select niu.notificacionInterna_id from NotificacionInterna_User niu where niu.contactos_id=" + u.id+")";
            } else {
                where = where + "id in (select niu.notificacionInterna_id from NotificacionInterna_User niu where niu.contactos_id=" + u.id+")";
            }
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
        Long count = type.count(search, searchFields, where);
        Long totalCount = type.count(null, null, where);
        try {
            //render(type, objects, count, totalCount, page, orderBy, order);
             render("NotificacionInternas/listRecived.html", type, objects, count, totalCount, page,
                    orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("NotificacionInternas/listRecived.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
        
       
    }
  
  
 public static void listRecivedNonLeidas(int page, String search, String searchFields,
            String orderBy, String order) {
        ObjectType type = ObjectType.get(getControllerClass());
        String where=type.createWhereFilterClausule();         
        User u=User.find("byUsuario", Seguridade.connected()).<User>first();
        orderBy="dataAlta";
        order="DESC";
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
        

         if (where == null || "".equals(where)) {
                where = "id in (select niu.notificacionInterna_id from NotificacionInterna_User niu where niu.contactos_id=" + u.id+" and niu.isLeido=false)";
            } else {
                where = where + "id in (select niu.notificacionInterna_id from NotificacionInterna_User niu where niu.contactos_id=" + u.id+" and niu.isLeido=false)";
            }
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
        Long count = type.count(search, searchFields, where);
        Long totalCount = type.count(null, null, where);
        try {
            //render(type, objects, count, totalCount, page, orderBy, order);
             render("NotificacionInternas/listRecivedNonLeidos.html", type, objects, count, totalCount, page,
                    orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("NotificacionInternas/listRecivedNonLeidos.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
        
       
    }  
  
  private static void marcarComoLeido(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());                     
        NotificacionInterna ni=(NotificacionInterna)type.findById(id);
        User u=User.find("byUsuario", Seguridade.connected()).<User>first();
        NotificacionInterna_User notificacionUser=NotificacionInterna_User.findByContactoENotificacion(u.id,id);
        notificacionUser.setIsLeido(true);
        notificacionUser._save();
    }
  
   public static void macarLeidoERepintar(String id,String page, String search) throws Exception {
   
            marcarComoLeido(id);
            listRecived(Integer.parseInt(page), search,null,null,null);            
  
   }
   
    public static void showNotificaion(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("NotificacionInternas/notificacionInterna.html", type, object);
        }
    }   
    
}