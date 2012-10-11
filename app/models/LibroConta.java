package models;

import controllers.Seguridade;
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
public class LibroConta extends UnionSecureModel {
    
    @Required
    @MaxSize(50)
    public String descricion;
        
    @AddForeignKey
    @NewForeignKey
    @ManyToMany
    public Set<FollaConta> follasContas;        
    
  
    @Required        
    public Date dataAlta;

    public Date dataBaixa;

  
    public LibroConta(String descricion, Set<FollaConta> follasContas,
            Date dataAlta,Date dataBaixa){
    	this.descricion=descricion;
        this.follasContas=follasContas;       
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
        
    }
      
    public String toString() {
        return this.descricion;
    }
    
    
    private static LibroConta createLibroContasAfiliacion(Organismo organismo){
        String descricion=play.i18n.Messages.get("libroContas.libroAutomaticoAfiliados");               
        LibroConta lc=new LibroConta(descricion, null, Tools.getCurrentDate(), null);
        TipoEstado te=TipoEstado.findById(Long.parseLong("1"));
        HashSet<FollaConta> fc=new HashSet<FollaConta>();
        lc.follasContas=fc;
        lc.setOrganismo(organismo);
        lc.setEstado(te);
        lc._save(); 
        return lc;
    }
    
       public static LibroConta getLibroContasAfiliacion(Organismo organismo){
        String descricion=play.i18n.Messages.get("libroContas.libroAutomaticoAfiliados");         
        LibroConta lc=LibroConta.find("byDescricionAndOrganismo",descricion, organismo).first();
        if(lc==null){
           lc= LibroConta.createLibroContasAfiliacion(organismo);
        }
        return lc;
    } 
       
    private static LibroConta createLibroContasPermanencia(Organismo organismo){
        String descricion=play.i18n.Messages.get("libroContas.libroAutomaticoPermanencia");               
        LibroConta lc=new LibroConta(descricion, null, Tools.getCurrentDate(), null);
        TipoEstado te=TipoEstado.findById(Long.parseLong("1"));
        HashSet<FollaConta> fc=new HashSet<FollaConta>();
        lc.follasContas=fc;
        lc.setOrganismo(organismo);
        lc.setEstado(te);
        lc._save(); 
        return lc;
    }
    
       public static LibroConta getLibroContasPermanencia(Organismo organismo){
        String descricion=play.i18n.Messages.get("libroContas.libroAutomaticoPermanecia");         
        LibroConta lc=LibroConta.find("byDescricionAndOrganismo",descricion, organismo).first();
        if(lc==null){
           lc= LibroConta.createLibroContasPermanencia(organismo);
        }
        return lc;
    }         
	
}
