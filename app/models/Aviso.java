package models;

import controllers.CRUD;
import java.util.*;
import javax.persistence.*;
import play.data.validation.Required;

import play.db.jpa.*;
import utils.AddForeignKey;

@Entity
public class Aviso extends Model {

    
    @Required
    @ManyToOne
    public TipoAviso tipoAviso;
        
    @Required 
    public String asunto;
    
    @Required
    public String contido;
    
    @ManyToMany
    @AddForeignKey
    public Set<Documento> docsAdxuntos;
    
    @Required
    @ManyToMany
    @AddForeignKey
    public Set<ListaDistribucion> contactos;
    
    public boolean foiEnviado;
    
    public Date dataRealizacionAviso;
    
    public Date dataARealizarAviso;
    
    @CRUD.Hidden
    public String avisoDe="rubendramos@gmail.com";

    public Aviso(){};
    
    public Aviso(TipoAviso tipoAviso,String asunto,String contido,Set<Documento> docsAdxuntos, Set<ListaDistribucion> contactos, 
    		Contacto envidoDe,boolean foiEnviado,Date dataRealizacionAviso,Date dataARealizarAviso,String avisoDe){
    	this.tipoAviso=tipoAviso;
    	this.asunto=asunto;
    	this.contido=contido;
    	this.docsAdxuntos=docsAdxuntos;
    	this.contactos=contactos;
    	this.foiEnviado=foiEnviado;
        this.dataARealizarAviso=dataARealizarAviso;
        this.dataRealizacionAviso=dataRealizacionAviso;
        this.avisoDe=avisoDe;
        
    	
    }
    
    public String toString(){
        
        return this.tipoAviso+" "+this.asunto+ "-"+ this.foiEnviado+"-"+ this.dataRealizacionAviso;
    
    }
        
    public List<String> getEmailsListasDistribucion() {        
        ArrayList res = new ArrayList();

        for (ListaDistribucion lista : this.contactos) {
            for (Contacto contacto : lista.contactos) {
                res.add(contacto.email);
            }
        }
        return res;        

    }
    

}