package controllers;

import controllers.Secure.Security;
import java.lang.reflect.Field;
import java.util.Random;
import models.User;
import play.*;
import play.db.Model;
import play.mvc.*;

@Check("secretariaXeral")
@With(Secure.class)
public class Users extends CRUD {    
    
    
 public static void password() {
        render();
    }
    

    public static void changePassword(String password, String verifyPassword) {
        String usuario=Security.connected();
        User connected =  User.find("byUsuario", usuario).<User>first();
        connected.password = password;
        validation.valid(connected);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, password).message(play.i18n.Messages.get("user.passwordNovaNonCoincide"));
        
        if(validation.hasErrors()) {
            render("@password", connected, verifyPassword);
        }
        connected.save();
        flash.success(play.i18n.Messages.get("user.passwordActualizada"));
        Privado.index();
    }    
    
}