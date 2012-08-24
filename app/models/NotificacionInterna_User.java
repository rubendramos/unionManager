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

   @Id 
   public Long notificacionInterna_id;
   @Id
   public Long contactos_id;     
   @CRUD.Hidden
   public boolean isLeido;
    

    
    public NotificacionInterna_User(Long notificacionInterna_id,Long contactos_id,boolean isLeido){
    	this.notificacionInterna_id=notificacionInterna_id;
    	this.contactos_id=contactos_id;
        this.isLeido=isLeido;
    
    }
    
    
 public static List<NotificacionInterna_User> findByContacto(Long contactoId) {
        String query = "from NotificacionInterna_User t  where t.contactos_id=" + contactoId+"";
        return find(query).fetch();
    }   
 
 
 
  public static NotificacionInterna_User findByContactoENotificacion(Long contactoId,Long notificacionId) {
        String query = "from NotificacionInterna_User t  where t.contactos_id=" + contactoId + " and t.notificacionInterna_id="+notificacionId;        
        return find(query).first();
    }  

  /**
     * @return the isLeido
     */
    public boolean isIsLeido() {
        return isLeido;
    }

    /**
     * @param isLeido the isLeido to set
     */
    public void setIsLeido(boolean isLeido) {
        this.isLeido = isLeido;
    } 
        
}