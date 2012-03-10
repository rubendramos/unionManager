package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import utils.AddForeignKey;
import utils.NewForeignKey;

import java.util.*;
import org.hibernate.annotations.Filter;
import utils.Tools;

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
    @AddForeignKey
    public Set<Afiliado> afiliados = new HashSet() ;
    
    @ManyToMany
    @AddForeignKey
    @NewForeignKey
    public Set<Evento> accions = new HashSet() ;
    
    @ManyToMany
    @AddForeignKey
    @NewForeignKey
    public Set<Documento> documentacion = new HashSet() ;    
  
  
    public Conflito(TipoConflito tipoConflito,String nome, String descricion, Date dataInicio, Date dataRemate,
            String valoracion,Set<Afiliado> afiliados,Set<Evento> accions,Set<Documento> documentacion){
    	this.tipoConflito=tipoConflito;
    	this.nome=nome;
    	this.descricion=descricion;
    	this.dataInicio=dataInicio;
    	this.dataRemate=dataRemate;
    	this.valoracion=valoracion;
    	this.afiliados=afiliados;
        this.accions=accions;
        this.documentacion=documentacion;
    }

    
    
    public String toString() {
        return this.tipoConflito.descricion +"  "+ this.nome +" "+ Tools.getLocaleDateFormat(this.dataInicio);
    }
 
}