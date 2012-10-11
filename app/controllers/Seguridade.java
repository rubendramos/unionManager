package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import models.*;
import utils.Tools;

public class Seguridade extends Secure.Security {

    static boolean authenticate(String username, String password) {          
        return User.connect(username, password) != null;
    }
    static boolean check(String profile) {
        User u=User.find("byUsuario", connected()).<User>first();
        if ("admin".equals(profile)) {
            return u.tenPermiso("Admin");
        }
        if ("comite".equals(profile)) {
            return u.tenPermiso("Comite");
        }
        if ("tesoureria".equals(profile)) {
            return u.tenPermiso("Tesoureria");
        }
        if ("propaganda".equals(profile)) {
            return u.tenPermiso("Propaganda");
        }
        if ("secretariaXeral".equals(profile)) {
            return u.tenPermiso("SXeral");
        }
        if ("accionSindical".equals(profile)) {
            return u.tenPermiso("Sindical");
        }
        if ("permanencia".equals(profile)) {
            return u.tenPermiso("Permanencia");
        }
        if ("organizacion".equals(profile)) {
            return u.tenPermiso("Organizacion");
        }
       if ("recibeNotificacions".equals(profile)) {
            //return User.find("byUsuario", connected()).<User>first().isRecibeNotificacions;
            return u.tenPermiso("Notificacion");
        }   
        if ("chat".equals(profile)) {
            //return User.find("byUsuario", connected()).<User>first().isRecibeNotificacions;
            return u.tenPermiso("Chat");
        } 
        
       if ("informes".equals(profile)) {
            //return User.find("byUsuario", connected()).<User>first().isRecibeNotificacions;
            return u.tenPermiso("Informes");
        }
       
        if ("autenticado".equals(profile)) {
            
            if(u!=null){
                return true;
            }else{
                return false;
            }
        }

        return false;
    }
    static boolean checkFuncionalidade(String funcionalidade) {
        User u=User.find("byUsuario", connected()).<User>first();   
        return u.tenPermisoFuncionalidade(funcionalidade);
    }
    
    
    
    static Organismo organismo() {
        return User.find("byUsuario", connected()).<User>first().organismo;
    }
    
    static User usuario() {
        return User.find("byUsuario", connected()).<User>first();
    }    

    static void onDisconnected() {
       
        Application.index();
    }
    
    static void onDisconnect() {
          User u=User.find("byUsuario", connected()).<User>first();
        if(u.tenPermiso("Admin")){
                u.setOrganismo(null);
                u._save();
        }
         
        }

    static void onAuthenticated() {
        User u=User.find("byUsuario", connected()).<User>first();
        if(u!=null){
        if(u.tenPermiso("Admin")){
            Organismos.organismoSeleccion();
        }else if(u.tenPermiso("Notificacion")){
            NotificacionInternas.listRecivedNonLeidas(1, null, null, null, null);
        }else{
            Privado.index();
        }
        }else{
            try {
                Secure.login();
            } catch (Throwable ex) {
                Logger.getLogger(Seguridade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}