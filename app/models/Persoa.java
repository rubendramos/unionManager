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
public class Persoa extends Model {
 
    @Email
    @Required
    @MaxSize(50)
    public String email;
    
    @Required
    @MaxSize(50)
    public String nome;
    
    @Required
    @MaxSize(50)
    public String apelido1;
    
    @MaxSize(50)
    public String apelido2;
    
    @Required
    @MaxSize(15)
    @AddFiltro
    public String dni;
    
    @Required
    @ManyToOne
    public Sexo sexo;
    
    @Required
    public Date dataNacemento;

    @Required
    @ManyToOne
    @AddForeignKey
    public Enderezo enderezo;    
    
    @MaxSize(50)
    @CRUD.Hidden
    public String nomeCompleto;
    
    
    
    
  
    public Persoa(String nome,String apelido1, String apelido2,String dni,String email,Sexo sexo,
    		Date dataNacemento,Enderezo enderezo){
    	this.apelido1=apelido1;
    	this.apelido2=apelido2;
    	this.dni=dni;
    	this.email=email;
    	this.nomeCompleto=getNomeCompleto();
    	this.dataNacemento=dataNacemento;
    	this.sexo=sexo;
    	this.nome=nome;
    	this.enderezo=enderezo;
    }

    
    public String getNomeCompleto() {
        return apelido1+" "+ apelido2+", "+ nome;
    } 
    
    public String getEmail() {
        return this.email;
    }     
    
    
    public String toString() {
        return this.dni+" "+this.nomeCompleto;
    }
 
}