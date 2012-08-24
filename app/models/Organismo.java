package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.NewForeignKey;
 
@Entity
public class Organismo extends UnionModel {
 
    
    @Required
    @ManyToOne
    public TipoOrganismo tipoOrganismo ;    
    
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
    @AddFiltro
    public Ramo ramo;
    
    @Email
    @MaxSize(50)
    public String email;
    
    
    
    @MaxSize(50)
    public String direccionWeb;
    
    @Required
    @AddFiltro
    public Date dataAlta;
    
    public Date dataBaixa;
        
    @Required
    @ManyToOne
    public Enderezo enderezo;
    
   
    @ManyToMany
    @AddForeignKey    
    public Set<Comite> comite;
    
    
   
    @ManyToMany
    @AddForeignKey  
    @NewForeignKey
    public Set<ComponenteOrganismo> componentes;
    
    @ManyToMany
    @AddForeignKey    
    public Set<Organismo> organismosFillo;
    
  
    public Organismo(TipoOrganismo tipoOrganismo,String nome,String acronimo,String descricion,Ramo ramo, Date dataAlta, 
            Date dataBaixa, Enderezo enderezo,Set<Comite> comite,Set<Organismo>organismosFillo,Set<ComponenteOrganismo> componentes){
        this.tipoOrganismo=tipoOrganismo;
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
 
}