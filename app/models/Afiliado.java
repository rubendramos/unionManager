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
public class Afiliado extends UnionModel {
 
    
    @Required
    @ManyToOne
    @AddForeignKey
    public Persoa persoa ;
 
    @Required
    @ManyToOne
    @AddFiltro
    private TipoEstadoAfiliado estadoAfiliado ;
    
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
    
    
    @ManyToOne
    @AddFiltro
    public Empresa empresa ;
    
    public String carnetConfederado;
    

    
  
    public Afiliado(Persoa persoa,Ocupacion ocupacion,Ramo ramo, Date dataAlta, Date dataBaixa, 
    		boolean milita,String carnetConfederado,Empresa empresa){
    	this.persoa=persoa;
    	this.ocupacion=ocupacion;
    	this.ramo=ramo;
    	this.dataAlta=dataAlta;
    	this.dataBaixa=dataBaixa;
    	this.milita=milita;
    	this.carnetConfederado=carnetConfederado;    
        this.empresa=empresa;
               
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

    /**
     * @return the dataBaixa
     */
    public Date getDataBaixa() {
        return dataBaixa;
    }

    /**
     * @param dataBaixa the dataBaixa to set
     */
    public void setDataBaixa(Date dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    /**
     * @return the estado
     */
    public TipoEstadoAfiliado getEstadoAfiliado() {
        return estadoAfiliado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstadoAfiliado(TipoEstadoAfiliado estadoAfiliado) {
        this.estadoAfiliado = estadoAfiliado;
    }
  
 
}
