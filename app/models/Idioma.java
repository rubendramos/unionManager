package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Idioma extends Model {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @MaxSize(2)
    public String sufixo;
  
    public Idioma(String descricion,String sufixo){
    	this.descricion=descricion;
        this.sufixo=sufixo;
    }
    
      
    public String toString() {
        return this.descricion+"-"+this.sufixo;
    }
	
}
