package controllers;

import models.Afiliado;
import models.Aviso;
import models.User;
import notificacions.Notificador;
import play.*;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class Afiliados extends CRUD {  
    

    public static void notificaAlta(Afiliado afiliado) {
        
        
        Aviso av=new Aviso();
        
        
        try {
            if (Notificador.notificacionXenerica(av)) {
                flash.success("Please check your emails ...");
                flash.put("email", av);
               
            }
        } catch (Exception e) {
            Logger.error(e, "Mail error");
        }
        flash.error("Oops (the email cannot be sent)...");
        flash.put("email", av);        
    }    
}