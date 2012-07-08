package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.AddFiltro;

@Entity
public class MensaxesIdioma extends Model {
    
    @Required
    @ManyToOne
    @AddFiltro
    public Idioma idioma;  
    
    @Required
    @ManyToOne
    @AddFiltro
    public Funcionalidade funcionalidade; 
    
    @Required
    @MaxSize(100)
    public String clave;    
    
    
    @Required
    @MaxSize(200)
    public String valor;
    
    
  
    public MensaxesIdioma(String clave,String valor,Idioma idioma,String cometario,Funcionalidade funcionalidade){
    	this.clave=clave;
        this.valor=valor;
        this.idioma=idioma;
        this.funcionalidade=funcionalidade;
    }
      
    public String toString() {
        return this.clave;
    }
	
}
