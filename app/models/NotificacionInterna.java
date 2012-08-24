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
    public Date dataAlta;

    @CRUD.Hidden
    @ManyToOne
    public User avisode;
    
    @Required
    @ManyToOne
    public TipoPrioridade prioridade;
    
  

    public NotificacionInterna(){};
    
    public NotificacionInterna(String asunto,String contido,Set<User> contactos, Date dataAlta,User avisode, TipoPrioridade prioridade){
    	this.asunto=asunto;
    	this.contido=contido;
        this.contactos=contactos;
        this.dataAlta=dataAlta;
        this.avisode=avisode;
        this.prioridade=prioridade;
       
    }
    
    
    public String toString(){
        
        return Tools.getLocaleDateFormat(this.dataAlta) +" - " + this.asunto;
    
    }
    
    

public static List<NotificacionInterna> findByContacto(Long contactoId) {
        String query = "select t from NotificacionInterna t, NotificacionInterna_User u where u.notificacionInterna_id=t.id and u.contactos_id='" + contactoId +"' order by  t.dataalta desc";        
        return find(query).fetch();
    }



    /**
     * @return the contactos
     */
    public Set<User> getContactos() {
        return contactos;
    }

    /**
     * @param contactos the contactos to set
     */
    public void setContactos(Set<User> contactos) {
        this.contactos = contactos;
    }

    /**
     * @return the user
     */
    public User getAvisode() {
        return avisode;
    }

    /**
     * @param user the user to set
     */
    public void setAvisode(User avisode) {
        this.avisode = avisode;
    }

   
}