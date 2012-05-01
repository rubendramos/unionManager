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
public class NotificacionInterna extends UnionModel {

            
    @Required 
    @MaxSize(100)
    public String asunto;
    
    @Required
    @Lob
    @MaxSize(500)
    public String contido;
    
    @Required
    @ManyToMany
    @AddForeignKey
    public Set<User> contactos;        
    
    @CRUD.Hidden
    public Date dataNotificacion;

    @CRUD.Hidden
    @ManyToOne
    public User user;
    
    @Required
    @ManyToOne
    public TipoPrioridade prioridade;
    

    public NotificacionInterna(){};
    
    public NotificacionInterna(String asunto,String contido,Set<User> contactos, Date dataNotificacion,User user, TipoPrioridade prioridade){
    	this.asunto=asunto;
    	this.contido=contido;
        this.contactos=contactos;
        this.dataNotificacion=dataNotificacion;
        this.user=user;
        this.prioridade=prioridade;
    }
    
    public String toString(){
        
        return Tools.getLocaleDateFormat(this.dataNotificacion) +" - " + this.asunto;
    
    }
    
public static List<NotificacionInterna> findByContactoOld(Long contactoId) {
        String query = "from NotificacionInterna t  where t.contactos.id=" + contactoId;        
        return find(query).fetch();
    }     


public static List<NotificacionInterna> findByContacto(Long contactoId) {
        String query = "select t from NotificacionInterna t, NotificacionInterna_User u where u.notificacionInterna_id=t.id and u.contactos_id=" + contactoId;        
        return find(query).fetch();
    } 
}