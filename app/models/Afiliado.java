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
public class Afiliado extends UnionModel implements Avisable {
 
    
    @Required
    @ManyToOne
    @AddForeignKey
    public Persoa persoa ;
       
    @Required
    @ManyToOne
    @AddFiltro
    public Ocupacion ocupacion;
    
    @Required
    @ManyToOne
    public Ramo ramo;
       
    @Required
    @AddFiltro
    public Date dataAlta;
    
    @AddFiltro
    public Date dataBaixa;
        
    public boolean milita;
    
    public String carnetConfederado;
    

    
  
    public Afiliado(Persoa persoa,Ocupacion ocupacion,Ramo ramo, Date dataAlta, Date dataBaixa, 
    		boolean milita,String carnetConfederado){
    	this.persoa=persoa;
    	this.ocupacion=ocupacion;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    	this.milita=milita;
    	this.carnetConfederado=carnetConfederado;    	
    }

    
    
    public String toString() {
        return persoa.dni+"-"+this.persoa.nomeCompleto;
    }

    public String getAsunto() {
        return "Afiliado "+ this.persoa.nomeCompleto;
    }

    public String getContido() {
        return "Afiliado" + this.persoa.nomeCompleto;
    }
    
   public String getsPersoa() {
        return this.persoa.toString();
    } 
   

     public void sendAviso(){
            Aviso aviso=Aviso.findById(Long.decode("1"));
            String asunto="Alta"+ this.organismo.acronimo+" "+ this.organismo.nome;
            String contido="Vostede foi dado de alta no" + this.organismo.acronimo+" "+ this.organismo.nome;
            aviso.setAsunto(asunto);
            aviso.setContido(contido);            
        }
    
 
 
}
