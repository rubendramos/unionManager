package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Ramo extends Model {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public Ramo(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
 
}