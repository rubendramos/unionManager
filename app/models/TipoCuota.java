package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.PlayCurrency;

@Entity
public class TipoCuota extends Model {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @MaxSize(10)
    @PlayCurrency
    public Double importe;
    
    public TipoCuota(String descricion){
    	this.descricion=descricion;
    }
      
    public String toString() {
        return this.descricion+"("+Double.toString(importe)+"€)";
    }
	
}
