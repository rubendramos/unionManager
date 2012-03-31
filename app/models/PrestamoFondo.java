package models;
 
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
 
@Entity
public class PrestamoFondo extends Model {
 
    @Required
    @ManyToOne    
    public EntradaFondo entradaFondo;
    
    @Required
    public Date dataPrestamo;
   
    public Date dataDevolucion;
    

    
  
    public PrestamoFondo(EntradaFondo entradaFondo,Date dataPrestamo, Date dataDevolucion){
    	this.entradaFondo=entradaFondo;    	
    	this.dataPrestamo=dataPrestamo;
    	this.dataDevolucion=dataDevolucion;
    }

    
    
    public String toString() {
        return this.entradaFondo.toString()+"-"+ this.dataPrestamo+"-"+ this.dataDevolucion;
    }

    
 
 
}
