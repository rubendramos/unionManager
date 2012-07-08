package controllers;

import com.lowagie.text.pdf.codec.Base64;
import play.*;
import play.mvc.*;
import sun.text.resources.FormatData;
import sun.text.resources.FormatData_es_ES;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;



import models.*;
import controllers.*;
import controllers.CRUD.ObjectType;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import javax.persistence.Parameter;
import models.InformeI;
import models.Informe;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import utils.Tools;

public class Informes extends  Controller {

	
      public static void show(String informe) throws Exception {       
        String sInformeClass="models.informes."+informe;
        ObjectType type=ObjectType.forClass(sInformeClass);
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("Informes/index.html", type, object);
        }
    }
     
     
    public static void informes() throws Exception {
        String nomeCalseInforme="models.informes."+params.get("claseInforme");
        ObjectType type = ObjectType.forClass(nomeCalseInforme);
        notFoundIfNull(type);

        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        InformeI object = (InformeI) constructor.newInstance();
        Organismo org = Seguridade.organismo();

        Binder.bindBean(params.getRootParamNode(), "object", object);
        renderBinary(object.xeneraInforme(org,params),object.getNomeInforme()+"."+object.getFormato());
        
    }     
  
  public static void index() {
      render();
  }  
}
