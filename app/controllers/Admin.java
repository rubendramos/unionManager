package controllers;
 
import play.*;
import play.mvc.*;
 
import java.util.*;
 
import models.*;

@Check("admin")
@With(Secure.class)
public class Admin extends Controller {
    
    @Before
    static void setConnectedUser() {
        if(Seguridade.isConnected()) {
            User user = User.find("byUsuario", Seguridade.connected()).first();
            renderArgs.put("user", user.toString());
        }
    }
 
    public static void index() {
        render();
    }
    
}