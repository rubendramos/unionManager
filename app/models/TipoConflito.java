package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class TipoConflito extends UnionModel {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public TipoConflito(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
	
}
