package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
 
@Entity
public class Localidade extends Model {
 
    
    @Required
    @MaxSize(50)
    public String descricion; 
    
    @Required
    @ManyToOne
    @AddFiltro
    public Provincia provincia;    
    
    @Required
    @ManyToOne
    @AddFiltro
    public ComunidadeAutonoma comunidadeAutonoma;
    
    public String latitude;
    public String lonxitude;
    
    @Required
    @MaxSize(5)
    public String codigoPostal;
    
  
    public Localidade(String descricion,Provincia provincia,ComunidadeAutonoma comunidadeAutonoma,
            String latitude,String lonxitude,String codigoPostal){
    	this.descricion=descricion;
        this.provincia=provincia;
        this.comunidadeAutonoma=comunidadeAutonoma;
        this.latitude=latitude;
        this.lonxitude=lonxitude;
        this.codigoPostal=codigoPostal;
    }
      
    public String toString() {
        return this.descricion+" - "+this.codigoPostal+" "+this.provincia+" ("+this.comunidadeAutonoma+")";
    }
 
}