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
public class Enderezo extends UnionModel {
	    
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
   
    @MaxSize(9)
    public String telefono;
    
    @MaxSize(9)
    public String fax;
    
    @MaxSize(9)
    public String apartadoCorreos;
    
    public Enderezo(String enderezo,Localidade localidade,String codigoPostal, String concello,String pais,String telefono,String fax,String apartadoCorreos){
    	this.enderezo=enderezo;
    	this.localidade=localidade;    
    	this.concello=concello;
    	this.pais=pais;    
        this.telefono=telefono; 
        this.fax=fax;
        this.apartadoCorreos=apartadoCorreos;
    }

    
    
    public String toString() {
        return this.enderezo +"-"+this.localidade.descricion +"-"+ this.concello +"-"+  this.localidade.codigoPostal+
                " "+ localidade.provincia+" ("+localidade.comunidadeAutonoma+")";
    }
 
}