package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
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
    @Sort(type=SortType.COMPARATOR,comparator= MensaxesIdioma.FuncionalidadeComparador.class)
    public Funcionalidade funcionalidade; 
    
    @Required
    @MaxSize(100)
    public String clave;    
    
    
    @Required
    @MaxSize(500)
    @Lob
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

public static class FuncionalidadeComparador implements Comparator<Funcionalidade> {

    public int compare(Funcionalidade fun1, Funcionalidade fun2) {
        String t1 = fun1.descricion;
        String t2 = fun2.descricion;

        if (t1 == null && t2 == null) {
            return 0;
        } else if (t1 == null) {
            return -1;
        } else if (t2 == null) {
            return 1;
        } else {
            return t1.toLowerCase().compareTo(t2.toLowerCase());
        }
    }
}    
    
    
}
