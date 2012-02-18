package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Conflito extends Model {
 
	@Required
	@MaxSize(50)
    public String nome ;
	
    @ManyToOne
    @Required
    public TipoConflito tipoConflito;	
	
	
	@Required
	@Lob
	@MaxSize(500)
	public String descricion;
	        
    
    public Date dataInicio;
        
    public Date dataRemate;
    
    @Lob
    @MaxSize(500)
    public String valoracion;
    
    @ManyToMany
    public Set<Afiliado> afiliados = new HashSet() ;
  
  
    public Conflito(TipoConflito tipoConflito,String nome, String descricion, Date dataInicio, Date dataRemate, String valoracion,Set<Afiliado> afiliados){
    	this.tipoConflito=tipoConflito;
    	this.nome=nome;
    	this.descricion=descricion;
    	this.dataInicio=dataInicio;
    	this.dataRemate=dataRemate;
    	this.valoracion=valoracion;
    	this.afiliados=afiliados;
    }

    
    
    public String toString() {
        return this.tipoConflito.descricion +"-"+ this.nome +"-"+ this.dataInicio;
    }
 
}