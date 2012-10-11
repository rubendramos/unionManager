package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.NewForeignKey;
import utils.Tools;
 
@Entity
public class Comite extends UnionSecureModel {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoOrganismo tipoComite;
    
    @Required
    @ManyToMany
    @AddForeignKey  
    @NewForeignKey  
    public Set<Secretaria> secretaria;
    
    @Required
    public Date dataAlta;
    
    
    public Date dataBaixa;
    
    public Comite(String descricion,TipoOrganismo tipoComite,Set<Secretaria> secretaria,Date dataAlta,Date dataBaixa){
        this.descricion=descricion;
    	this.tipoComite=tipoComite;
        this.secretaria=secretaria;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
    }
      
    public String toString() {
        String dataB=this.dataBaixa==null ? ")": ")/("+Tools.getLocaleDateFormat(this.dataBaixa)+")";
        return this.tipoComite+" - "+this.descricion+"("+ Tools.getLocaleDateFormat(dataAlta)+dataB;
    }
 
}