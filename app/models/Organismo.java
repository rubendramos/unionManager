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
 
@Entity
@FilterDef(name="organismo", parameters= { @ParamDef( name="organismo_id", type="long") }, defaultCondition="((id = :organismo_id))" )
@Filter(name="organismo")
public class Organismo extends UnionModel {
 
    
    @Required
    @ManyToOne  
    public TipoOrganismo tipoOrganismo ;    
    
    @Required    
    public String nome ;
  
    
    public String descricion;
    
    @Required
    @ManyToOne
    @AddFiltro
    public Ramo ramo;
      
    @Required
    @AddFiltro
   
    public Date dataAlta;
    
    
    @Required    
    @MaxSize(15)
    public String cif ;
    
    @Required    
    public String acronimo;
    
   
    
    @Email
    @MaxSize(50)
    public String email;
    
    
    
    @MaxSize(50)
    public String direccionWeb;
       
   
    public Date dataBaixa;
        
    @Required
    @ManyToOne
    @AddForeignKey
    @NewForeignKey
    public Enderezo enderezo;
    
   
    @ManyToMany
    @AddForeignKey    
    @NewForeignKey
    public Set<Comite> comite;
    
    
   
    @ManyToMany
    @AddForeignKey  
    @NewForeignKey
    public Set<ComponenteOrganismo> componentes;
    
    @ManyToMany
    @AddForeignKey    
    @NewForeignKey
    public Set<Organismo> organismosFillo;
    
  
    public Organismo(TipoOrganismo tipoOrganismo,String nome,String acronimo,String descricion,Ramo ramo, Date dataAlta, 
            Date dataBaixa, Enderezo enderezo,Set<Comite> comite,Set<Organismo>organismosFillo,Set<ComponenteOrganismo> componentes){                
        this.tipoOrganismo=tipoOrganismo;
        this.descricion=descricion;
    	this.nome=nome;
    	this.acronimo=acronimo;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    	this.enderezo=enderezo;
        this.comite=comite;
        this.organismosFillo=organismosFillo;
        this.componentes=componentes;
    }

   
    
    
    public String toString() {
        return this.acronimo +"-"+ this.nome;
    }
    
    
      public static List<Organismo> listFillos(Long organismoId) {
            String query = "select orgorg from Organismo org   "
                + "join org.organismosFillo orgorg "                            
                + "where  org.id='"+organismoId+"'";                   
        return find(query).fetch();
        
    } 

      
}