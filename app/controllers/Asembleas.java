package controllers;

import com.mysql.jdbc.Blob;
import java.util.*;
import models.*;
import play.*;
import play.db.Model;
import play.db.jpa.GenericModel;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import utils.Tools;

@Check("comite")
@With(Secure.class)
public class Asembleas extends CRUD {    
    

public static void list(int page,String where, String search,String from,String searchFields,
            String orderBy, String order) {
        ObjectType type = ObjectType.get(getControllerClass());
        String whereClausule=null;
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
   
        if(from!=null && "true".equals(from)){
            if(where!=null && !"".equals(where)){
            whereClausule=where;
            }
            type.setValuesFromSearch(where);       
        
        }else{
            whereClausule=type.createWhereFilterClausule();
        }
        
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);
        Long count = type.count(search, searchFields, whereClausule);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("asembleas/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }      
    
    
  public static void mandarAvisoAsemblea(String id,String page, String search) throws Exception {
        
      
      ObjectType type = ObjectType.get(getControllerClass());
      String where=type.createWhereFilterClausule();
      Asemblea asemblea=Asemblea.findById(Long.parseLong(id));
      TipoEstadoAviso tsa=TipoEstadoAviso.findById(Long.parseLong("1"));
      ListaDistribucion li= ListaDistribucion.getListaAutomaticaAfiliados();
      Set<ListaDistribucion> sli=new HashSet<ListaDistribucion>();
      sli.add(li);            
      
      Aviso aviso=new Aviso(null,asemblea,asemblea.titulo,asemblea.toString(),null,sli,tsa,null,null);
      aviso.organismo=Seguridade.organismo();
      aviso._save();
      flash.success(play.i18n.Messages.get("crud.avisoGardado", asemblea.toString()));
      list(Integer.parseInt(page), where,search, "false",null, null, null);
  
    }    
           
    
}