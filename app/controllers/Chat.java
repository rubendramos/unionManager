package controllers;

import java.io.File;
import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;
import play.vfs.VirtualFile;


@Check("chat")
@With(Secure.class)
public class Chat extends Controller {

    public static void index() {
        render();
    }
    
  
    public static void conversaComite() {
            User user = User.find("byUsuario", Seguridade.connected()).first();
            WebSocket.room(user.afiliado.persoa.nome,user.organismo.id.toString());               
    }  
    
    public static void downloadChatLog(String idOrganismo) {       
        String chatLogPath=Play.configuration.getProperty("chatLog.path")+"chatLog"+idOrganismo+".txt";
        File f= VirtualFile.fromRelativePath(chatLogPath).getRealFile();
        renderBinary(f,"log");        
    }     

}