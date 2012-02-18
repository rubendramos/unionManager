package models;
 
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.ManyToAny;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Enderezo extends Model {
	    
    @Required
    public String enderezo ;
    
    @Required
    public String localidade;
    
    @Required
    public String codigoPostal;
       
    @Required
    public String concello;
    
    public String provincia;
        
    public String pais;
   
  
    public Enderezo(String enderezo,String localidade,String codigoPostal, String concello, String provincia, String pais){
    	this.enderezo=enderezo;
    	this.localidade=localidade;
    	this.codigoPostal=codigoPostal;
    	this.concello=concello;
    	this.provincia=provincia;
    	this.pais=pais;    	
    }

    
    
    public String toString() {
        return this.enderezo +"-"+ this.localidade +"-"+ this.codigoPostal +"-"+ this.concello +"("+ provincia+")";
    }
 
}