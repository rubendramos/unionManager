package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
 
@Entity
public class Comite extends Model {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoComite tipoComite;
    
    @Required
    @ManyToMany
    @AddForeignKey    
    public Set<Secretaria> secretaria;
    
    @Required
    public Date dataAlta;
    
    
    public Date dataBaixa;
    
    public Comite(String descricion,TipoComite tipoComite,Set<Secretaria> secretaria,Date dataAlta,Date dataBaixa){
        this.descricion=descricion;
    	this.tipoComite=tipoComite;
        this.secretaria=secretaria;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
    }
      
    public String toString() {
        return this.descricion+"-"+this.tipoComite+"("+ dataAlta+ this.dataBaixa==null ? ")": ")/"+this.dataBaixa+")";
    }
 
}