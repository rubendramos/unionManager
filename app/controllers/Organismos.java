package controllers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import models.*;
import play.*;
import play.cache.Cache;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import play.utils.Java;

@Check("admin")
@With(Secure.class)
public class Organismos extends CRUD {    
  
    
    
    
 public static void list() {
        try {
               ObjectType type = ObjectType.get(getControllerClass());
               String where=type.createWhereFilterClausule();         
               User u=User.find("byUsuario", Seguridade.connected()).<User>first();
               
               int page=0;
               String orderBy="dataAlta";
               String order="DESC";
               
               notFoundIfNull(type);
               if (page < 1) {
                   page = 1;
               }
               
               String search="";
               String searchFields="";
               
               
               
                if (where == null || "".equals(where)) {
                       //where = "id in (select orgorg.id from models.Organismo orgorg where orgorg.id='" + u.organismo.id+"')";
                    //where="id='"+u.organismo.id+"'";   
                   } else {
                       where = where + "id in (select orgorg.organismosFillo from models.Organismo orgorg where orgorg.id='" + u.organismo.id+"')";
                    //where="where  id='"+u.organismo.id+"'";   
                   }
               
                
                  List<Organismo> objects= Organismo.listFillos(u.organismo.id);
                  Organismo org=(Organismo)type.findById(u.organismo.id.toString());
                  objects.add(org);
                  
              // List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
               
               Long count = Long.parseLong(Integer.valueOf(objects.size()).toString());
               Long totalCount = Long.parseLong(Integer.valueOf(objects.size()).toString());
               try {
                   //render(type, objects, count, totalCount, page, orderBy, order);
                    render("Organismos/listFillos.html", type, objects, count, totalCount, page,
                           orderBy, order);
               } catch (TemplateNotFoundException e) {
                   render("Organismos/listFillos.html", type, objects, count, totalCount, page,
                           orderBy, order);
               }
               
              
           } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }    
    
    
 public static void xeneraFichaOrganismo(String idOrganismo,String page, String search) {
         
        Map reportParams = new HashMap();           
            
            if (idOrganismo==null || "".equals(idOrganismo)){
                idOrganismo="-1";
            }
            
            String nomeInforme="fichaOrganismo";
            
	    reportParams.put("ORGANISMO_ID",idOrganismo);           
            reportParams.put("STITULO",play.i18n.Messages.get("informe.fichaOrganismo.titulo"));           
            reportParams.put("SCIF",play.i18n.Messages.get("informe.fichaOrganismo.cif"));            
            reportParams.put("SNOME",play.i18n.Messages.get("informe.fichaOrganismo.nomeSindicato"));
            reportParams.put("SACRONIMO",play.i18n.Messages.get("informe.fichaOrganismo.acronimoSindicato"));
            reportParams.put("SDATAALTA",play.i18n.Messages.get("informe.fichaOrganismo.dataalta"));
            reportParams.put("SDATABAXIA",play.i18n.Messages.get("informe.fichaOrganismo.databaixa"));
            reportParams.put("SNOMEAFILIADO",play.i18n.Messages.get("informe.fichaOrganismo.nomeAfiliados"));
            reportParams.put("STITCULOCOMPONENTES",play.i18n.Messages.get("informe.fichaOrganismo.titulocomponentes"));
                    reportParams.put("STITCOMITE",play.i18n.Messages.get("informe.fichaOrganismo.titComites"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.fichaOrganismo.eMail"));
            reportParams.put("SWEB",play.i18n.Messages.get("informe.fichaOrganismo.web"));
            
            reportParams.put("SENDEREZO",play.i18n.Messages.get("informe.generic.enderezo"));
            reportParams.put("SLOCALIDADE",play.i18n.Messages.get("informe.generic.localidade"));
            reportParams.put("SCODIGOPOSTAL",play.i18n.Messages.get("informe.generic.codigoPostal"));
            reportParams.put("SCONCELLO",play.i18n.Messages.get("informe.generic.concello"));
            reportParams.put("SPROVINCIA",play.i18n.Messages.get("informe.generic.provincia"));
            reportParams.put("SCOMUNIDADE",play.i18n.Messages.get("informe.generic.comunidade"));
            
            
            reportParams.put("SNOMESINDICATO",play.i18n.Messages.get("informe.fichaOrganismo.sindicato"));
            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SAPARTADOCORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
            reportParams.put("SSECRETARIA",play.i18n.Messages.get("informe.fichaOrganismo.secretaria"));
            reportParams.put("TITULOENDEREZOORGANISMI",play.i18n.Messages.get("informe.fichaOrganismo.tituloEnderezo"));
            reportParams.put("SDATOSORGANISMO",play.i18n.Messages.get("informe.fichaOrganismo.datosOrganismo"));
            reportParams.put("SDESCRICION",play.i18n.Messages.get("informe.fichaOrganismo.descricion"));
            
            
            
	    renderBinary(utils.BaseJasperReports.download_pdf(nomeInforme,reportParams),nomeInforme+".pdf");
            
    }   
 
 
  public static void altaOrganismoBlank(){
        try {
              ObjectType type = ObjectType.forClass("models.OrganismoSimple");
              
              
              //ObjectType type = (ObjectType) Java.invokeStaticOrParent(controllers.Organismos.class, "createObjectType", models.OrganismoSimple.class);
              notFoundIfNull(type);
              Constructor<?> constructor;
              OrganismoSimple object=null;
              try {
                  constructor = type.entityClass.getDeclaredConstructor();
                  constructor.setAccessible(true);
                  object = (OrganismoSimple) constructor.newInstance();       
                  render(type, object, null, null, null);
              } catch (InstantiationException ex) {
                  java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalAccessException ex) {
                  java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalArgumentException ex) {
                  java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InvocationTargetException ex) {
                  java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
              } catch (TemplateNotFoundException e) {
                  render("Organismos/altaOrganismo.html", type, object, null, null, null);
      //            render("CRUD/blank.html", type, object, null, null, null);
              } catch (NoSuchMethodException ex) {
                  java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
              } catch (SecurityException ex) {
                  java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
              }
          
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
        }
    
  }
  
  
  
  public static void saveOrganismo(){
        try {
       
        
        //ObjectType type = ObjectType.get(getControllerClass());
        ObjectType type = ObjectType.forClass("models.OrganismoSimple");

        notFoundIfNull(type);


        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        OrganismoSimple object = (OrganismoSimple) constructor.newInstance();

        String where = params.get("where");
        String search = params.get("search");
        String page = params.get("page");
        String from = params.get("from");

        Binder.bindBean(params.getRootParamNode(), "object", object);

        
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/Organismos/altaOrganismo.html",
                        type, object, page, where, search, from);
            } catch (TemplateNotFoundException e) {
            render("Organismos/altaOrganismo.html", type, object, null, null, null);
            }
        }

        object._save();
        Organismo o=Organismo.findById(object.id);
        object.setOrganismo(o);
        object._save();

        //Creamos a lista de distribucion automatica para o centro
        TipoListaDistribucion tld = TipoListaDistribucion.findById(Long.parseLong("1"));
        ListaDistribucion ld = new ListaDistribucion(play.i18n.Messages.get("listaAutomaticaAfiliados"), tld, null, null);         
        ld.setOrganismo(o);
        ld._save();
        
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));             
        cambiaOrganismo(object.id.toString());        

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Organismos.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        }

  
     public static void organismoSeleccion() {
        List<Organismo> organismos =Organismo.findAll();
        render(organismos);
    }   
    
   public static void cambiaOrganismo(String organismo) {
      if(organismo==null || "".equals(organismo)){
          flash.error(play.i18n.Messages.get("organismo.requerido"));
          organismoSeleccion();
      }else{
           Organismo organismoO=Organismo.findById(Long.parseLong(organismo));
        
        User u = User.find("byUsuario", Seguridade.connected()).<User>first();
        u.setOrganismo(organismoO);
        u._save();
        session.put("sindicato", organismoO);
        flash.success(play.i18n.Messages.get("organismo.organismoSeleccionado",organismoO.acronimo+"-"+organismoO.nome.toString()));        
        NotificacionInternas.listRecivedNonLeidas(1, null, null, null, null);
        //render("Privado/index.html");    
      }
                   
    }     
  
    }