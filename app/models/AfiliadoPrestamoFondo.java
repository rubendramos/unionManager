package models;
 
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.NewForeignKey;
 
@Entity
public class AfiliadoPrestamoFondo extends UnionModel {
 
    @Required
    @ManyToOne   
    @AddFiltro
    public Afiliado afiliado;
    
    @Required
    @ManyToMany
    @AddForeignKey
    @NewForeignKey
    public Set<PrestamoFondo> prestamoFondo;
  
    public AfiliadoPrestamoFondo(Set<PrestamoFondo> prestamoFondo,Afiliado afiliado){
    	this.prestamoFondo=prestamoFondo;    	    	
    	this.afiliado=afiliado;
    }
    
    public String toString() {
        return this.afiliado.toString()+"-"+ this.prestamoFondo;
    }

    
 
 
}
