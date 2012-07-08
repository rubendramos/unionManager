package controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.*;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class Eventos extends CRUD {    
    
public static void list(int page, String search, String searchFields,
            String orderBy, String order) {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
   
        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, type.createWhereFilterClausule());
        Long count = type.count(search, searchFields, type.createWhereFilterClausule());
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
      ListaDistribucion li= ListaDistribucion.getListaAutomaticaAfiliados();
      Set<ListaDistribucion> sli=new HashSet<ListaDistribucion>();
      sli.add(li);            
      
      Aviso aviso=new Aviso(evento,null,evento.nome,evento.descricion,null,sli,tsa,null,null);
      aviso.organismo=Seguridade.organismo();
      aviso._save();
      flash.success(play.i18n.Messages.get("crud.avisoGardado", evento.toString()));
      list(Integer.parseInt(page), search, null, null, null);
  
    }     
    
}
