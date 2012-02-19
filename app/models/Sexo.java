package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Sexo extends Model {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public Sexo(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
 
}