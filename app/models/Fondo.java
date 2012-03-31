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
public class Fondo extends Model {
 
   
    
    @Required
    @MaxSize(50)
    public String nome;
    
    @Required
    @MaxSize(500)
    public String descricion;
 
    public boolean ePublico;
    
    @Required
    @ManyToOne
    public Organismo sindicato;
    
    @Transient
    public Set<EntradaFondo> entradas;
    
  
    public Fondo(String nome,boolean ePublico,String descricion,Organismo sindicato,Set<EntradaFondo> entradas){
    	this.nome=nome;
    	this.descricion=descricion;
        this.ePublico=ePublico;
    	this.sindicato=sindicato;
        this.entradas=entradas;
    }

    
    
    public String toString() {
        return this.nome+"-"+this.descricion;
    }

    
 
 
}
