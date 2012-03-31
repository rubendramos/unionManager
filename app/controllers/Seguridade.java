package controllers;
 
import models.*;
 
public class Seguridade extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
    	//return true;
    	return User.connect(username, password) != null;
    }
    
    static boolean check(String profile) {
        if("admin".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isAdmin;
        }
        if("comite".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isComite;
        }
        if("tesoureria".equals(profile)) {
            return User.find("byUsuario", connected()).<User>first().isTesoureiro;
        }        
        return false;
    }    
    
    static void onDisconnected() {
        Application.index();
    }
    

    static void onAuthenticated() {
        Privado.index();
    }    
    
}