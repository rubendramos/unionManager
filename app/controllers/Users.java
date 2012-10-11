package controllers;

import controllers.Secure.Security;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import models.*;
import notificacions.Notificador;
import org.hibernate.Filter;
import org.hibernate.Session;
import play.*;
import play.db.Model;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import utils.Tools;

@Check("secretariaXeral")
@With(Secure.class)




public class Users extends CRUD {    
    
    
  public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) throws Exception {
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
        if (whereClausule==null){
            whereClausule= "id!=1 and usuario!='admin'";
        }else{
            whereClausule=whereClausule+" and id!=1 and usuario!='admin'";
        }
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);
        Long count = type.count(search, searchFields, whereClausule);
        Long totalCount = type.count(null, null, "id!=1 and usuario!='admin'");
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }
  
  
   private static void darDeAltaUsuario(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        User userLogueado = User.find("byUsuario", Seguridade.connected()).<User>first();
        User userAlta = (User) type.findById(id);
        userAlta.dataBaixa=null;
        userAlta.dataAlta=Tools.getCurrentDate();
        userAlta.estadoUsuario=((TipoEstadoUsuario) TipoEstadoUsuario.findById(Long.parseLong("1"))); 
        userAlta.enviarNotificacionInternaAlta();        
        userAlta._save();
        userAlta.mandarEmailAltaUsuario(userLogueado);
    }

    private static void darDeBaixaUsuario(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        User userLogueado = User.find("byUsuario", Seguridade.connected()).<User>first();
        User userBaixa = (User) type.findById(id);        
        userBaixa.dataBaixa=Tools.getCurrentDate();
        userBaixa.estadoUsuario=((TipoEstadoUsuario) TipoEstadoUsuario.findById(Long.parseLong("2")));   
        userBaixa.mandarEmailBaixaUsuario(userLogueado);
        userBaixa._save();                
    }  
    
    public static void darDebaixaUsuarioERepintar(String id, String page, String search) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        String where = type.createWhereFilterClausule();
        darDeBaixaUsuario(id);
        list(Integer.parseInt(page), where, search, "false", null, null, null);

    }

    public static void darDeAltaUsuarioERepintar(String id, String page, String search) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        String where = type.createWhereFilterClausule();
        darDeAltaUsuario(id);
        list(Integer.parseInt(page), where, search, "false", null, null, null);

    }    
    
}
    
    
     
         
    
