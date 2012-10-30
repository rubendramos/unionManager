package models;
 
import controllers.CRUD;
import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.NewForeignKey;
 
@Entity
public class AutorEntrada extends UnionModel {
    
    @Required
    @MaxSize(50)
    public String nome;
    
    @Required
    @MaxSize(50)
    public String apelido1;
    
    @MaxSize(50)
    public String apelido2;
    
    @MaxSize(50)
    @CRUD.Hidden
    public String nomeCompleto;
        
    @Lob
    @MaxSize(500)
    public String resena;
    
    
  
    public AutorEntrada(String nome,String apelido1, String apelido2,String resena){
    	this.apelido1=apelido1;
    	this.apelido2=apelido2;    	
    	this.nomeCompleto=getNomeCompleto();    	
    	this.nome=nome;
        this.resena=resena;
    	
    }

    
    public String getNomeCompleto() {
        String sApelido1="";
        String sApelido2="";
        String sNome="";
        if(apelido1!=null && !"".equals(apelido1)){
            sApelido1=apelido1;
        }
        
        if(apelido2!=null && !"".equals(apelido2)){
            sApelido2=" "+apelido2;
        }        
        
        if(nome!=null && !"".equals(nome)){
            sNome=", "+nome;
        }                
        return (sApelido1+sApelido2+sNome).toLowerCase().trim();
    } 
    
    
    public String toString() {
        return this.nomeCompleto;
    }
 
}