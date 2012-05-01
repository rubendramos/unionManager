package models;
 
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
 
@Entity
public class Provincia extends UnionModel {
 
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @ManyToOne
    @AddFiltro
    public ComunidadeAutonoma comunidade;
    
  
    public Provincia(String descricion, ComunidadeAutonoma comunidade){
    	this.descricion=descricion;
        this.comunidade=comunidade;
    }
      
    public String toString() {
        return this.descricion;
    }
 
}