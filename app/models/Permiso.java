package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import utils.AddForeignKey;

@Entity
@FilterDef(name="permiso",  defaultCondition="((id != 1))" )
@Filter(name="permiso")
public class Permiso extends Model {
    
    @Required
    @MaxSize(50)
    public String nome;
    
    
    @Required
    @MaxSize(150)
    @Lob
    public String descricion;
        
    @AddForeignKey
    @ManyToMany
    public Set<Funcionalidade> funcionalidade;
        

  
    public Permiso(String nome, String descricion,Set<Funcionalidade> funcionalidade){
    	this.nome=nome;
        this.descricion=descricion;
        this.funcionalidade=funcionalidade;
        
    }
      
    public String toString() {
        return this.nome+" - "+ this.descricion;
    }
	
}
