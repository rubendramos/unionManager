package controllers;

import com.sun.org.apache.bcel.internal.generic.LASTORE;
import java.util.List;
import models.Idioma;
import models.User;
import play.db.jpa.GenericModel;
import play.mvc.*;
import utils.LoadPropertiesFiles;

@Check("admin")
@With(Secure.class)
public class Idiomas extends CRUD {    
    
    
    
    public static void idiomas() {
        List<Idioma> idiomas =Idioma.findAll();
        render(idiomas);
    }    
    
    public static void cambiaIdioma(String idiomas) {
        LoadPropertiesFiles lpf=new LoadPropertiesFiles();
        Idioma idiomaO=Idioma.findById(Long.parseLong(idiomas));
        lpf.loadMessagesIdioma(idiomaO);
        flash.success(play.i18n.Messages.get("idioma.idiomaActualizado",idiomaO.descricion));
        idiomas();
    }       
}