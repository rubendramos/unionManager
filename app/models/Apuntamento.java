package models;

import controllers.CRUD;
import controllers.Seguridade;
import java.lang.reflect.Constructor;
import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import play.data.binding.Binder;
import play.exceptions.TemplateNotFoundException;
import utils.AddFiltro;
import utils.PlayCurrency;
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
    @PlayCurrency
    public Double importe;
  
    
    @Required
    @AddFiltro
    public Date dataApuntamento;
    
      
    public Blob adxunto;
    
    
    public Date dataRexistro;
    
    @CRUD.Hidden
    @ManyToOne
    public User usuarioApuntamento;
    
    public Apuntamento(TipoApuntamento tipoApuntamento,TipoConcepto concepto,String descricion,Double importe, 
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
	
     public static Apuntamento createApuntamentoCuotaAfiliado(User u,Afiliado af,Organismo organismo, Mes mes,Ano ano){
        String descricion=play.i18n.Messages.get("apuntamento.apuntamentoCuota",af.persoa.nomeCompleto,mes.descricion,ano.descricion);               
        TipoApuntamento ta=TipoApuntamento.findById(Long.parseLong("1"));
        TipoConcepto tc=TipoConcepto.findById(Long.parseLong("5"));
        TipoEstado te=TipoEstado.findById(Long.parseLong("1"));
        Apuntamento ap=new Apuntamento(ta,tc,descricion,af.tipoCuotaAfiliado.importe,null, Tools.getCurrentDate(), u);
        ap.setEstado(te);
        ap.setOrganismo(organismo);
        ap._save();    
        return ap;
    }  
     
     public static Apuntamento getApuntamentoCuotaAfiliado(User u,Afiliado af,Organismo organismo,Mes mes,Ano ano){
        String descricion=play.i18n.Messages.get("apuntamento.apuntamentoCuota",af.persoa.nomeCompleto,mes.descricion,ano.descricion);               
        return find("byDescricionAndOrganismo",descricion,organismo).first();           
    }  
     
     public static Apuntamento createApuntamentoVendaFondos(User u,VendaFondo vendaFondo,Organismo organismo){
        String descricion="";
        TipoApuntamento ta=null;
        
         if (vendaFondo.dataDevolucionVenda==null || "".equals(vendaFondo.dataDevolucionVenda)){
           descricion=play.i18n.Messages.get("apuntamento.apuntamentoVendaFondos",Integer.toString(vendaFondo.nUnidades),vendaFondo.entradaFondo.toString());               
           ta=TipoApuntamento.findById(Long.parseLong("1"));
         }else{
           descricion=play.i18n.Messages.get("apuntamento.apuntamentoDevolucionVendaFondos",Integer.toString(vendaFondo.nUnidades),vendaFondo.entradaFondo.toString());                           
           ta=TipoApuntamento.findById(Long.parseLong("2"));
        }        
        TipoConcepto tc=TipoConcepto.findById(Long.parseLong("6"));
        TipoEstado te=TipoEstado.findById(Long.parseLong("1"));
       
        Apuntamento ap=new Apuntamento(ta,tc,descricion,vendaFondo.importeVenda,null, Tools.getCurrentDate(), u);
        ap.setEstado(te);
        ap.setOrganismo(organismo);
        ap._save();    
        return ap;
    }  
     
     public static Apuntamento getApuntamentoVendaFondos(Afiliado af,Organismo organismo,VendaFondo vendaFondo){
         String descricion="";
         if (vendaFondo.dataDevolucionVenda==null || "".equals(vendaFondo.dataDevolucionVenda)){
           descricion=play.i18n.Messages.get("apuntamento.apuntamentoVendaFondos",Integer.toString(vendaFondo.nUnidades),vendaFondo.entradaFondo.toString());                        
         }else{
           descricion=play.i18n.Messages.get("apuntamento.apuntamentoDevolucionVendaFondos",Integer.toString(vendaFondo.nUnidades),vendaFondo.entradaFondo.toString());                                    
        }    
        return find("byDescricionAndOrganismo",descricion,organismo).first();           
    }       
     
     
   public String getFormatoMoeda(){
       return Tools.getCurrency(this.importe);
       
   } 
    
}
