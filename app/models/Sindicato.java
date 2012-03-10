package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddForeignKey;
 
@Entity
public class Sindicato extends Model {
 
    
    @Required    
    public String nome ;
    
    @Required    
    @MaxSize(15)
    public String cif ;
    
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
    
    @Required
    @ManyToMany
    @AddForeignKey    
    public Set<Comite> comite;
    
  
    public Sindicato(String nome,String acronimo,String descricion,Ramo ramo, Date dataAlta, 
            Date dataBaixa, Enderezo enderezo,Set<Comite> comite){
    	this.nome=nome;
    	this.acronimo=acronimo;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    	this.enderezo=enderezo;
        this.comite=comite;
    }

    
    
    public String toString() {
        return this.acronimo +"-"+ this.nome;
    }
 
}