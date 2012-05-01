package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
 
@Entity
public class Ocupacion extends UnionModel {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    

    @Required
    @ManyToOne
    @AddFiltro
    public Ramo ramo;    
    
  
    public Ocupacion(String descricion, Ramo ramo){
    	this.descricion=descricion;
    	this.ramo=ramo;
    }
      
    public String toString() {
        return this.descricion + " (" +ramo.descricion+")";
    	
    }
 
}