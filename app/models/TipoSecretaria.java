package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;


@Entity
public class TipoSecretaria extends Model {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public TipoSecretaria(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
	
}
