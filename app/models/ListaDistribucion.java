package models;
 
import controllers.Contactos;
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
 
@Entity
public class ListaDistribucion extends Model {
 
    
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoListaDistribucion tipoListaDistribucion;

    @Required
    @MaxSize(50)
    public String descricion;   
    
    
    @ManyToMany
    @AddForeignKey    
    public Set<Contacto> contactos;
        
    @Lob
    @MaxSize(500)
    public String sentenciaSQL;
   
    public ListaDistribucion(String descricion,TipoListaDistribucion tipoListaDistribucion,
            Set<Contacto> contactos,String sentenciaSQL){
        this.descricion=descricion;
        this.tipoListaDistribucion=tipoListaDistribucion;
    	this.contactos=contactos;
        this.sentenciaSQL=sentenciaSQL;
        
    }
      
    public String toString() {
        return this.tipoListaDistribucion + "-"+this.descricion;
    }
 
}