package models;
 
import controllers.CRUD;
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
 
@Entity
public class Empresa extends UnionModel {
 
    @Required
    @AddFiltro
    @ManyToOne
    public TipoEmpresa tipoEmpresa; 
   
    @Required
    @MaxSize(15)    
    public String cif;
    
    @Required
    @MaxSize(50)
    public String nome;
    
    
    public String descricion;
    
    @Required
    @AddFiltro
    @ManyToOne
    public Ramo ramo; 
    
    @Email
    @Required
    @MaxSize(50)
    public String email;
        
    @MaxSize(9)
    public String telefono;           

    @Required
    @ManyToOne
    @AddForeignKey
    public Enderezo enderezo;    
        
            
  
    public Empresa(TipoEmpresa tipoEmpresa,String nome, String descricion,String cif,String email,Enderezo enderezo,String telefono,
            Ramo ramo){
    	this.cif=cif;
    	this.email=email;
    	this.nome=nome;
        this.tipoEmpresa=this.tipoEmpresa;
    	this.enderezo=enderezo;        
        this.telefono=telefono;
        this.ramo=ramo;
    }

    public String toString() {
        return this.tipoEmpresa+"-"+this.ramo+"-"+this.nome;
    }
 
}