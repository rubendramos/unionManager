package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
 
@Entity
public class Secretaria extends UnionModel {
 
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoSecretaria tipoSecretaria;
    
    @Required
    @ManyToOne
    @AddForeignKey    
    public Afiliado secretario;
    
    @Required
    public Date dataAlta;
        
    public Date dataBaixa;
  
    public Secretaria(TipoSecretaria tipoSecretaria,Afiliado secretario,Date dataAlta,Date dataBaixa){
    	this.tipoSecretaria=tipoSecretaria;
        this.secretario=secretario;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
    }
      
    public String toString() {
        return this.tipoSecretaria+"-"+ this.secretario +"("+ dataAlta+ (this.dataBaixa==null ? ")": ")/"+this.dataBaixa+")");
    }
 
}