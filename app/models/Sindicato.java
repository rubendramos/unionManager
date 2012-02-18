package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Sindicato extends Model {
 
    
    @Required    
    public String nome ;
    
    @Required    
    public String acronimo;
    
    public String descricion;
    
    @Required
    @ManyToOne
    public Ramo ramo;
       
    @Required
    public Date dataAlta;
    
    public Date dataBaixa;
        
    @Required
    @ManyToOne
    public Enderezo enderezo;
    
  
    public Sindicato(String nome,String acronimo,String descricion,Ramo ramo, Date dataAlta, Date dataBaixa, Enderezo enderezo){
    	this.nome=nome;
    	this.acronimo=acronimo;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    	this.enderezo=enderezo;
    }

    
    
    public String toString() {
        return this.acronimo +"-"+ this.nome;
    }
 
}