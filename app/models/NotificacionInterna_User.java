package models;

import controllers.CRUD;
import java.util.*;
import javax.persistence.*;
import net.sf.oval.constraint.MaxLength;
import play.data.validation.MaxSize;
import play.data.validation.Required;

import play.db.jpa.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.Tools;

@Entity
public class NotificacionInterna_User extends Model {

            
   public String notificacionInterna_id;
   public String contactos_id;     

    
    public NotificacionInterna_User(String notificacionInterna_id,String contactos_id){
    	this.notificacionInterna_id=notificacionInterna_id;
    	this.contactos_id=contactos_id;
    
    }
    
    
 public static List<NotificacionInterna_User> findByContacto(Long contactoId) {
        String query = "from NotificacionInterna_User t  where t.contactos_id=" + contactoId;        
        return find(query).fetch();
    }    

  
        
}