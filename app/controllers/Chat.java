package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;

public class Chat extends Controller {

    public static void index() {
        render();
    }
    
  
    public static void conversaComite() {
            User user = User.find("byUsuario", Seguridade.connected()).first();
            WebSocket.room(user.afiliado.persoa.nome);               
    }    

}