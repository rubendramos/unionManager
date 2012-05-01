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
public class EntradaFondo extends UnionModel {
 
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoFondo tipoFondo;
       
    @Required
    @ManyToOne
    @AddFiltro
    public TipoGeneroFondo tipoGeneroFondo;
    
    @Required
    @MaxSize(10)
    public String sinatura;
    
    @Required
    @MaxSize(100)
    @AddFiltro
    public String titulo;
    
    @MaxSize(500)
    public String descricion;
 
    @Required
    @MaxSize(50)
    @AddFiltro
    public String autor;    
    
    @Required
    @MaxSize(4)
    public String anoEdicion;
        
    public boolean ePrestable;
    
    public Blob caratula;       
    
  
    public EntradaFondo(TipoFondo tipoFondo,TipoGeneroFondo tipoGeneroFondo,String titulo, String anoEdicion, 
            String autor,boolean ePrestable,String descricion,Blob caratula,String sinatura){
    	this.tipoFondo=tipoFondo;
    	this.tipoGeneroFondo=tipoGeneroFondo;
    	this.titulo=titulo;
    	this.anoEdicion=anoEdicion;
    	this.autor=autor;
    	this.caratula= caratula;
    	this.descricion=descricion;
        this.ePrestable=ePrestable;
        this.sinatura=sinatura;
    }

    
    
    public String toString() {
        return this.tipoFondo.toString()+"-"+this.tipoGeneroFondo.toString()+" - "+ this.titulo;
    }

    
 
 
}
