package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ano extends Model {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public Ano(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
	
    public static Cuota getAnoPorDescricion(String ano){
        return find("byDescricion",ano).first();
    }
}
