package controllers;

import java.io.File;
import play.*;
import play.mvc.*;
import utils.Tools;

@Check("autenticado")
@With(Secure.class)
public class Documentacions extends CRUD {    
    
    public static  void documentacion(){
    render();
        }
    
    public static void renderDoc(String nome){
        String pathDocumentacion= Play.configuration.getProperty("documentacion.path");        
        String path=pathDocumentacion+nome;
         File f=Tools.getFileFromPath(path);
        renderBinary(f);
    }
}