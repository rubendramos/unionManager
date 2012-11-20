package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.ParamDef;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.NewForeignKey;
 


@javax.persistence.Entity
@Table(name = "Organismo")
public class OrganismoSimple extends UnionModel {
 
    
    @Required
    @ManyToOne  
    public TipoOrganismo tipoOrganismo ;    
    
    @Required    
    public String nome ;
    
    @Required    
    public String acronimo;
  
    
    public String descricion;
    
    @Required
    @ManyToOne
    @AddFiltro
    public Ramo ramo;
      
    @Required
    @AddFiltro
   
    public Date dataAlta;
    
   
  
    public OrganismoSimple(TipoOrganismo tipoOrganismo,String nome,String descricion,Ramo ramo, Date dataAlta,String acronimo){       
        this.tipoOrganismo=tipoOrganismo;
        this.descricion=descricion;
    	this.nome=nome;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
        this.acronimo=acronimo;
    }

    
    
    public String toString() {
        return this.acronimo+"-"+this.nome;
    }
 
}