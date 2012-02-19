package controllers;
 
import play.*;
import play.mvc.*;
 
import java.util.*;
 
import models.*;

@Check("comite")
@With(Secure.class)
public class Privado extends Controller {
    
    @Before
    static void setConnectedUser() {
        if(Seguridade.isConnected()) {
            User user = User.find("byUsuario", Seguridade.connected()).first();
            renderArgs.put("user", user.afiliado.toString());
        }
    }
 
    public static void index() {
        render();
    }
    
}