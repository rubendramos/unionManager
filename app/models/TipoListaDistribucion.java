package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

@Entity
@FilterDef(name="tipolistadistribucion", defaultCondition="((id != '1'))" )
@Filter(name="tipolistadistribucion")
public class TipoListaDistribucion extends Model {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
  
    public TipoListaDistribucion(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion;
    }
	
}
