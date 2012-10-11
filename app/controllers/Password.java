package controllers;

import controllers.Secure.Security;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.logging.Level;
import models.User;
import play.*;
import play.db.Model;
import play.mvc.*;

@Check("autenticado")
@With(Secure.class)
public class Password extends CRUD {

    public static void password() {
        render();
    }

    public static void forgotPassword(String email) {
        String mensaxe="";
        validation.required(email);
        validation.email(email);
        if (validation.hasErrors()) {
            render("Password/recuperarPassword.html");
        }
        
        
        User u=User.find("byUsuario", email).<User>first();
        if(u!=null && u.estadoUsuario.id==1){            
            try {
                u.mandarEmailRecuperaPassword();
                 mensaxe=play.i18n.Messages.get("password.emailRecuperaPasswordEnviado",email);
                 
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
                mensaxe=play.i18n.Messages.get("generic.erroProcesandoAPeticion");
                render("Password/emailEnviadoRecuperarPassword.html",mensaxe);
            }           
           
        }else{
            mensaxe=play.i18n.Messages.get("password.usuarioNonExiste",email);            
        }
        
        render("Password/emailEnviadoRecuperarPassword.html",mensaxe);
    }

    public static void changePassword(String password, String verifyPassword, String passwordActual) {
        String usuario = Security.connected();
        User connected = User.find("byUsuario", usuario).<User>first();
        String passwordEnBaseDatos = connected.password;

        validation.valid(password);
        validation.required(passwordActual);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, password).message(play.i18n.Messages.get("user.passwordNovaNonCoincide"));
        validation.equals(passwordActual, passwordEnBaseDatos).message(play.i18n.Messages.get("user.passwordActualNonCoincide"));
        connected.password = password;

        if (validation.hasErrors()) {
            render("@password", connected, verifyPassword, passwordActual);
        } else {

            connected.save();
            flash.success(play.i18n.Messages.get("user.passwordActualizada"));
            Privado.index();
        }
    }
}