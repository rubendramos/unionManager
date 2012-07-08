package models.informes;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.*;

import play.db.jpa.*;
import play.mvc.Scope.Params;
import utils.Tools;

 
 
@Entity
public class InformeFondos extends Informe{
    
    @ManyToOne
    public TipoEntradaFondo tipoFondo;
    @ManyToOne
    public Fondo fondo;
    
   
    

  
    public InformeFondos(TipoEntradaFondo tipoFondo,Fondo fondo) {
    	this.tipoFondo=tipoFondo;        
        this.fondo=fondo;       
       
    }
      
    public String toString() {
        return this.fondo==null ?"":" "+this.fondo.toString();
    }

    public InputStream xeneraInforme(Organismo org,Params params) {
         
        Map reportParams = new HashMap();   
        InformeFondos informe=this;            
            String sTipoFondo="-1";
            String sFondo="-1";
            
                                   
            if(informe.tipoFondo!=null){
                sTipoFondo=informe.tipoFondo.id.toString();
            }

            if(informe.fondo!=null){
                sFondo=informe.fondo.id.toString();
            }
            
        
            
            
            
	    
            reportParams.put("FONDO_ID", sFondo);
	    reportParams.put("ORGANISMO_ID", org.id.toString());
	    reportParams.put("TIPOENTRADA_ID", sTipoFondo);
            reportParams.put("TITULO",play.i18n.Messages.get("informe.fondo.titulo"));
            reportParams.put("SUTTITULO",getSubtitulo());
            reportParams.put("STITULOENTRADA",play.i18n.Messages.get("informe.fondo.tituloentrada"));
            reportParams.put("SDESCRICIONENTRADA",play.i18n.Messages.get("informe.fondo.descricionentrada"));
            
            reportParams.put("SAUTORENTRADA",play.i18n.Messages.get("informe.fondo.autorentrada"));
            reportParams.put("SENTRADAPRESTABLE",play.i18n.Messages.get("informe.fondo.entradaprestable"));
            reportParams.put("SANOEDICIONENTRADA",play.i18n.Messages.get("informe.fondo.anoedicion"));
            reportParams.put("SFONDO",play.i18n.Messages.get("informe.fondo.fondo"));
            reportParams.put("SSINATURAENTRADA",play.i18n.Messages.get("informe.fondo.sinatura"));
            reportParams.put("STIPOENTRADA",play.i18n.Messages.get("informe.fondo.stipoentrada"));
            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
               	    	       
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String getSubtitulo(){
        String res="";

        if(this.tipoFondo!=null && !"".equals(this.tipoFondo.toString())){ 
            res=res+play.i18n.Messages.get("informe.fondo.tipoconflito")+": "+this.tipoFondo+".";
         }
        
        if(this.fondo!=null && !"".equals(this.fondo.toString())){ 
            res=res+play.i18n.Messages.get("informe.fondo.tipoconflito")+": "+this.fondo+".";
         }
        
         return res;
    }
    

    public String getNomeInforme() {
        return play.i18n.Messages.get("informe.fondo.nome")+ (this.fondo==null ?"":" "+this.fondo.toString());
    }

    public String getInforme() {
        return "fondos";
    }
    
     public String getFormato() {
        return "pdf";
    }
    
    
    
 
}
