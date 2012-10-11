package controllers;

import controllers.Secure.Security;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        if(Security.isConnected()){
            Seguridade.onAuthenticated();
        }else{
        
            render();
        }

}
}