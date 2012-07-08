package controllers;

import models.*;

public class Seguridade extends Secure.Security {

    static boolean authenticate(String username, String password) {
        //return true;
        return User.connect(username, password) != null;
    }

    static boolean check(String profile) {
        if ("admin".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isAdmin;
        }
        if ("comite".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isComite;
        }
        if ("tesoureria".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isTesoureiro;
        }
        if ("propaganda".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isPropaganda;
        }
        if ("permanencia".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isPermanencia;
        }
        if ("secretariaXeral".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isSecretariaXeral;
        }
        if ("accionSindical".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isAccionSindical;
        }
        if ("permanencia".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isPermanencia;
        }
        if ("organizacion".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isOrganizacion;
        }
       if ("recibeNotificacions".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isRecibeNotificacions;
        }        
        if ("autenticado".equals(profile)) {
            User u=User.find("byUsuario", connected()).<User>first();
            if(u!=null){
                return true;
            }else{
                return false;
            }
        }

        return false;
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

    static void onAuthenticated() {
        NotificacionInternas.listRecivedNonLeidas(1, null, null, null, null);
    }
}