package models;
 
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
 
@Entity
public class Provincia extends Model {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @ManyToOne
    public ComunidadeAutonoma comunidade;
    
  
    public Provincia(String descricion, ComunidadeAutonoma comunidade){
    	this.descricion=descricion;
        this.comunidade=comunidade;
    }
      
    public String toString() {
        return this.descricion;
    }
 
}