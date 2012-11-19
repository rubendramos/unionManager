package models;
 
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.NewForeignKey;
 
@Entity
@FilterDef(name="fondoPublico", defaultCondition="((epublico='TRUE'))" )
@Filter(name="fondoPublico")
public class Fondo extends UnionModel {
 
   
    
    @Required
    @MaxSize(50)
    public String nome;
    
    @Required
    @MaxSize(500)
    public String descricion;
 
    public boolean ePublico;
    
    @Required
    @MaxSize(2)
    public int periodoDiasPrestamo;
    
    @Required
    @MaxSize(2)
    public int diasDemoraAvisoVencemento;
    
    @Required
    @MaxSize(1)
    public int umaxPrestamo;


    
  
    public Fondo(String nome,boolean ePublico,String descricion,int periodoDiasPrestamo,int umaxPrestamo,int diasDemoraAvisoVencemento){
    	this.nome=nome;
        this.umaxPrestamo=umaxPrestamo;
        this.periodoDiasPrestamo=periodoDiasPrestamo;
    	this.descricion=descricion;
        this.ePublico=ePublico;   
        this.diasDemoraAvisoVencemento=diasDemoraAvisoVencemento;
    }

    
    
    public String toString() {
        return this.nome;
    }

    
 
 
}
