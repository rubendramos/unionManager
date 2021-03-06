package controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.*;
import org.apache.commons.lang.StringUtils;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class Eventos extends CRUD {    
@Before
     static void setDefaultOrder() {
	if(StringUtils.isBlank(request.params.get("orderBy"))) {
		request.params.put("order", "DESC");
		request.params.put("orderBy", "dataRealizacion");
	}
}
public static void list(int page,  String where,String search,String from,String searchFields,
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
            render("eventos/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }     
    
  public static void mandarAvisoEvento(String id,String page, String search) throws Exception {
        
      
      Evento evento=Evento.findById(Long.parseLong(id));
      TipoEstadoAviso tsa=TipoEstadoAviso.findById(Long.parseLong("1"));
      ListaDistribucion li= ListaDistribucion.getListaAutomaticaAfiliados(Seguridade.organismo());
      Set<ListaDistribucion> sli=new HashSet<ListaDistribucion>();
      sli.add(li);            
      
      Aviso aviso=new Aviso(evento);
      aviso.setFirma(Seguridade.usuario());
      aviso.organismo=Seguridade.organismo();
      aviso._save();
      flash.success(play.i18n.Messages.get("crud.avisoGardado", evento.toString()));
      list(Integer.parseInt(page), search, null,null,null, null, null);
  
    }     
    
}
