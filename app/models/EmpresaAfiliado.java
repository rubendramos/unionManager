package models;
 
import models.*;
import controllers.CRUD;
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
 
@Entity
public class EmpresaAfiliado extends UnionModel {
 
    @Required
    @ManyToOne
    public Afiliado afiliado; 
   
    
    @Required    
    @ManyToOne
    public Empresa empresa; 
    

    @Required
    public Date dataAlta;    
        
    public Date dataBaixa;    
            
  
    public EmpresaAfiliado(Afiliado afiliado,Empresa empresa,Date dataAlta,Date dataBaixa){
    	this.afiliado=afiliado;
    	this.empresa=empresa;
    	this.dataAlta=dataAlta;
        this.dataBaixa=this.dataBaixa;
    }

    public String toString() {
        return this.afiliado+"-"+this.empresa+"-"+this.dataAlta;
    }
 
}