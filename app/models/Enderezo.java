package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddForeignKey;
import utils.NewForeignKey;
 
@Entity
public class Enderezo extends Model {
	    
    @Required
    public String enderezo ;
    
    @Required
    @ManyToOne
    @AddForeignKey
    @NewForeignKey
    public Localidade localidade;
         
    @Required
    public String concello;
    
    public String pais;
   
  
    public Enderezo(String enderezo,Localidade localidade,String codigoPostal, String concello,String pais){
    	this.enderezo=enderezo;
    	this.localidade=localidade;    
    	this.concello=concello;
    	this.pais=pais;    	
    }

    
    
    public String toString() {
        return this.enderezo +"-"+this.localidade.descricion +"-"+ this.concello +"-"+  this.localidade.codigoPostal+
                " "+ localidade.provincia+" ("+localidade.comunidadeAutonoma+")";
    }
 
}