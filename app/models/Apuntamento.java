package models;

import controllers.CRUD;
import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.AddFiltro;
import utils.Tools;

@Entity
public class Apuntamento extends UnionSecureModel {
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoApuntamento tipoApuntamento;

    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoConcepto concepto;        
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required       
    public Float importe;
  
    
    @Required
    @AddFiltro
    public Date dataApuntamento;
    
      
    public Blob adxunto;
    
    
    public Date dataRexistro;
    public User usuarioApuntamento;
    
    public Apuntamento(TipoApuntamento tipoApuntamento,TipoConcepto concepto,String descricion,Float importe, 
            Blob adxunto,Date dataApuntamento, User usuarioApuntamento){
    	this.descricion=descricion;
        this.adxunto=adxunto;
        this.concepto=concepto;
        this.importe=importe;
        this.tipoApuntamento=tipoApuntamento;
        this.usuarioApuntamento=usuarioApuntamento;
        this.dataApuntamento=dataApuntamento;
    }
      
    public String toString() {
        return this.tipoApuntamento+" "+ this.concepto+":  "+ this.descricion+" ("+ Tools.getLocaleDateFormat(this.dataApuntamento)+")......."+this.importe+"€";
    }
	
}
