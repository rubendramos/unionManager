package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.AddForeignKey;
import utils.NewForeignKey;
import utils.Tools;

@Entity
public class FollaConta extends UnionModel {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @AddForeignKey
    @NewForeignKey
    @ManyToMany
    public Set<Apuntamento> apuntamentos;
    
    @Required
    public Date dataAlta;
    
    public Date dataBaixa;        
  
    public FollaConta(String descricion, Set<Apuntamento> apuntamentos,Date dataAlta,Date dataBaixa){
    	this.descricion=descricion;
        this.apuntamentos=apuntamentos;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
    
    }
      
    public String toString() {
        return this.descricion;
    }
	
}
