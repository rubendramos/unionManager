package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.AddForeignKey;

@Entity
public class LibroConta extends Model {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @AddForeignKey
    @ManyToMany
    public Set<FollaConta> follasContas;
    
    @Required    
    @ManyToOne
    public Organismo sindicato;
    
  
    @Required        
    public Date dataAlta;

    public Date dataBaixa;

  
    public LibroConta(String descricion, Set<FollaConta> follasContas,Organismo sindicato,
            Date dataAlta,Date dataBaixa){
    	this.descricion=descricion;
        this.follasContas=follasContas;
        this.sindicato=sindicato;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
        
    }
      
    public String toString() {
        return this.descricion+" - "+ this.follasContas+"-" +this.sindicato;
    }
	
}
