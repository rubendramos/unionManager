package models;
 
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.NewForeignKey;
 
@Entity
public class Cuota extends UnionModel {
 
    
    @Required  
    @ManyToOne
    @AddFiltro
    public Ano ano ;
          
    @Required
    @ManyToOne
    @AddFiltro
    public Mes mes ;
    
    
    @Required
    @ManyToOne
    @AddForeignKey
    public Afiliado afiliado ;
    
    @Required
    public boolean isPagado;
    

    
  
    public Cuota(Afiliado afiliado,Ano ano,Mes mes,boolean isPagado){
    	this.afiliado=afiliado;
    	this.ano=ano;    	
        this.mes=mes;
        this.isPagado=isPagado;
               
    }

    
    
    public String toString() {
        return ano+"-"+mes+"-"+afiliado+"-"+isPagado;
    }

    public String getEstadoPagamento(){
        return this.isPagado ?"Pagado":"Non pagado";
    }
    
    public static Cuota getMaxCuota(){
        return Cuota.find("from Cuota c where c.mes=(select max(mes) from Cuota) and c.ano=(select max(ano) from Cuota)").first();
    }    
    
 
    
}
