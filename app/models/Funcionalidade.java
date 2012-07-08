package models;

import models.*;
import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.AddFiltro;

@Entity
public class Funcionalidade extends Model {
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoFuncionalidade tipoFuncionalidade;
    
    @Required
    @MaxSize(50)
    public String nome;

    
    
    @MaxSize(100)
    public String descricion;
    
  
    public Funcionalidade(String descricion,TipoFuncionalidade tipoFuncionalidade,String nome){
    	this.descricion=descricion;
        this.nome=nome;
        this.tipoFuncionalidade=tipoFuncionalidade;
    }
      
    public String toString() {
        return this.tipoFuncionalidade+" - "+ this.descricion;
    }
	
}
