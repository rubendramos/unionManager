package controllers;


import java.lang.reflect.Constructor;
import java.util.List;
import javax.persistence.Query;
import models.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import play.Play;

import play.data.binding.Binder;
import play.db.Model;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.i18n.Lang;
import play.mvc.*;
import play.vfs.VirtualFile;
import utils.LoadPropertiesFiles;

@Check("admin")
@With(Secure.class)
public class Idiomas extends CRUD {    
    
    
    
    public static void seleccionaIdioma() {
        List<Idioma> idiomas =Idioma.findAll();
        render(idiomas);
    }    
    
     public static void cambiarIdioma(String idiomas) {
        Idioma idiomaO=Idioma.findById(Long.parseLong(idiomas));
//        LoadPropertiesFiles lpf=new LoadPropertiesFiles();
//        lpf.addMessagesIdioma(idiomaO);
        Lang.change(idiomaO.sufixo);        
        flash.success(play.i18n.Messages.get("idioma.idiomaActualizado",idiomaO.descricion));     
        seleccionaIdioma();
        
    }  
     
     public static int creaEntradasMensaxesIdioma(String idIdioma) {
          
          Query query = JPA.em().createNativeQuery("insert into mensaxesidioma select (select max(id) from mensaxesidioma)+rownum id,clave clave,valor valor ,funcionalidade_id funcionalidade_id ,'"+ idIdioma+"' idioma_id from mensaxesidioma where idioma_id='1'");
          return query.executeUpdate();
     }       
     
    public static void create() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);


        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        

        String where=params.get("where");
        String search=params.get("search");
        String page=params.get("page");
        String from=params.get("from");
        
        Binder.bindBean(params.getRootParamNode(), "object", object);        


        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/blank.html",
                        type, object,page,where,search,from);
            } catch (TemplateNotFoundException e) {
                render("CRUD/blank.html", type, object,page,where,search,from);
            }
        }
       
        object._save();
        int addEntradas=creaEntradasMensaxesIdioma(object._key().toString());
        if(addEntradas==0){
            object._delete();
            flash.error(play.i18n.Messages.get("crud.mensaxesIdiomaError"));
            redirect(request.controller + ".list",page,where,search,from);
            
        }
        
        LoadPropertiesFiles lpf=new LoadPropertiesFiles();
        Idioma idiomaO=Idioma.findById(object._key());
        lpf.addMessagesIdioma(idiomaO);
        
        String langs=Play.configuration.getProperty("application.langs");
        langs=langs+","+idiomaO.sufixo;
        lpf.loadAplicatonProperties("application.langs",langs);
        
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list",page,where,search,from);
        }
        if (params.get("_saveAndAddAnother") != null) {
            redirect(request.controller + ".blank",page,where,search,from);
        }


        redirect(request.controller + ".show", object._key(),page,where,search,from);
    }     
}