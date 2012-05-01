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
public class PersoaPrestamoFondo extends UnionModel {
 
 
    
    @Required
    @ManyToOne
    @AddForeignKey
    public Persoa persoa ;

   @Required
   @ManyToMany
   @AddForeignKey
    public Set<PrestamoFondo> prestamoFondo;    
 
    
  
    public PersoaPrestamoFondo(Persoa persoa,Set<PrestamoFondo> prestamoFondo){
    	this.prestamoFondo=prestamoFondo;    	
    	this.persoa=persoa;
    }
 
    public String toString() {
        return this.persoa.nomeCompleto+"-"+this.prestamoFondo.toString();
    }

   
}
