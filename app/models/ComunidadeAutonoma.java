package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class ComunidadeAutonoma extends UnionModel {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public ComunidadeAutonoma(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
 
}