package models;
 
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.Tools;
 
@Entity
public class ComponenteOrganismo extends UnionModel {
 
    
    @Required
    @ManyToOne    
    public Afiliado afiliado ;
       
    @Required
    @AddFiltro
    public Date dataAlta;
    
     
    @AddFiltro
    private Date dataBaixa;
        
    

    
  
    public ComponenteOrganismo(Afiliado afiliado, Date dataAlta, Date dataBaixa){
    	this.afiliado=afiliado;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    }

    
    
    public String toString() {
        String dataB=this.dataBaixa==null ? ")": ")/("+Tools.getLocaleDateFormat(this.dataBaixa)+")";
        return this.afiliado.persoa.nomeCompleto+"-("+ Tools.getLocaleDateFormat(dataAlta)+dataB;        
    }

   
}
