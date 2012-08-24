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
    public TipoGeneroFondo tipoXenero;
    @ManyToOne
    public Fondo fondo;
    
    
   
    

  
    public InformeFondos(TipoEntradaFondo tipoFondo,Fondo fondo,TipoGeneroFondo tipoXenero) {
    	this.tipoFondo=tipoFondo;        
        this.fondo=fondo;  
        this.tipoXenero=tipoXenero;
       
    }
      
    public String toString() {
        return this.fondo==null ?"":" "+this.fondo.toString();
    }

    public InputStream xeneraInforme(Organismo org,Params params) {
         
        Map reportParams = new HashMap();   
        InformeFondos informe=this;            
            String sTipoFondo="-1";
            String sFondo="-1";
            String sXenero="-1";
            
                                   
            if(informe.tipoFondo!=null){
                sTipoFondo=informe.tipoFondo.id.toString();
            }

            if(informe.fondo!=null){
                sFondo=informe.fondo.id.toString();
            }
            
            if(informe.tipoXenero!=null){
                sFondo=informe.tipoXenero.id.toString();
            }            
            
        
            
            
            
	    
            reportParams.put("FONDO_ID", sFondo);
	    reportParams.put("ORGANISMO_ID", org.id.toString());
	    reportParams.put("TIPOENTRADA_ID", sTipoFondo);
            reportParams.put("TIPOENTRADAGENERO_ID", sXenero);
            reportParams.put("TITULO",play.i18n.Messages.get("informe.fondo.titulo"));
            reportParams.put("SUBTITULO",getSubtitulo());
            reportParams.put("STITULOENTRADA",play.i18n.Messages.get("informe.fondo.tituloentrada"));
            reportParams.put("SDESCRICIONENTRADA",play.i18n.Messages.get("informe.fondo.descricionentrada"));
            
            reportParams.put("SAUTORENTRADA",play.i18n.Messages.get("informe.fondo.autorentrada"));
            reportParams.put("SENTRADAPRESTABLE",play.i18n.Messages.get("informe.fondo.entradaprestable"));
            reportParams.put("SANOEDICIONENTRADA",play.i18n.Messages.get("informe.fondo.anoedicion"));
            reportParams.put("SFONDO",play.i18n.Messages.get("informe.fondo.fondo"));
            reportParams.put("SSINATURAENTRADA",play.i18n.Messages.get("informe.fondo.sinatura"));
            reportParams.put("STIPOENTRADA",play.i18n.Messages.get("informe.fondo.stipoentrada"));
            reportParams.put("SXENERO",play.i18n.Messages.get("informe.fondo.xenero"));
            reportParams.put("SFONDODESCATALOGADOS",play.i18n.Messages.get("informe.fondo.sfondodescatalogado"));
            reportParams.put("SDESFONDODESCATALOGADOS",play.i18n.Messages.get("informe.fondo.sdesfondodescatalogado"));
           
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
            
            reportParams.put("SSI",play.i18n.Messages.get("generic.si"));
            reportParams.put("SNO",play.i18n.Messages.get("generic.no"));
               	    	       
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String getSubtitulo(){
        String res="";

        if(this.tipoFondo!=null && !"".equals(this.tipoFondo.toString())){ 
            res=res+play.i18n.Messages.get("informe.fondo.stipoentrada")+": "+this.tipoFondo+".";
         }
        
        if(this.fondo!=null && !"".equals(this.fondo.toString())){ 
            res=res+play.i18n.Messages.get("informe.fondo.fondo")+": "+this.fondo.nome+".";
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
