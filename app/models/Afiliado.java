package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Afiliado extends Model {
 
    
    @Required
    @ManyToOne
    public Persoa persoa ;
    
    @Required
    @ManyToOne
    public Ocupacion ocupacion;
    
    @Required
    @ManyToOne
    public Ramo ramo;
       
    @Required
    public Date dataAlta;
    
    public Date dataBaixa;
        
    public boolean milita;
    
    public String carnetConfederado;
    
    @Required
    @ManyToOne    
    public Sindicato sindicato;
    
    
    
    
  
    public Afiliado(Persoa persoa,Ocupacion ocupacion,Ramo ramo, Date dataAlta, Date dataBaixa, 
    		boolean milita,String carnetConfederado,Sindicato sindicato){
    	this.persoa=persoa;
    	this.ocupacion=ocupacion;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    	this.milita=milita;
    	this.carnetConfederado=carnetConfederado;
    	this.sindicato=sindicato;
    }

    
    
    public String toString() {
        return this.persoa.nomeCompleto;
    }
    
 
 
}