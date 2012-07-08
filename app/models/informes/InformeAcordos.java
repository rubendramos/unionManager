package models.informes;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.*;
import org.apache.commons.configuration.ConfigurationException;

import play.db.jpa.*;
import play.mvc.Scope.Params;
import utils.AddForeignKey;
import utils.LoadPropertiesFiles;
import utils.Tools;

 
 
@Entity
public class InformeAcordos extends Informe{
    
    @ManyToOne
    public TipoPuntoAsemblea tipoPuntoAsemblea;    
    @ManyToOne
    public Afiliado aPeticionDe;    
    public Date dataCelebracion;
    public boolean soConAcordos;

    
    

  
    public InformeAcordos(TipoPuntoAsemblea tipoPuntoAsemblea,Date dataCelecracion,boolean soConAcordos,Afiliado aPeticionDe) {
    	this.tipoPuntoAsemblea=tipoPuntoAsemblea;  
        this.aPeticionDe=aPeticionDe;
        this.dataCelebracion=dataCelebracion;     
        this.soConAcordos=soConAcordos;
    }
      
    public String toString() {
        return getNomeInforme();
    }

    public InputStream xeneraInforme(Organismo org,Params params) {
         
        Map reportParams = new HashMap();           
            String sDataCelebracionIni="-1";
            
            String sDataCelebracionFin="-1";
            String sTipoPuntoAsemblea="-1";
            String sAsemblea="-1";
            String sSoAcordos="-1";
            String sApetidionDe="-1";
            
            
            
            String dataAltaIni= params.get("object.dataCelebracion.ini");
            String dataAltaFin=params.get("object.dataCelebracion.fin");

                                   
            if(this.tipoPuntoAsemblea!=null){
                sTipoPuntoAsemblea=this.tipoPuntoAsemblea.id.toString();
            }

            if(this.soConAcordos){
                sSoAcordos="1";
            }
     
            if(dataAltaIni!=null && !"".equals(dataAltaIni)){
                sDataCelebracionIni=Tools.dateToDateDataBaseFormat(dataAltaIni);
            }
            
            
            if(aPeticionDe!=null && !"".equals(aPeticionDe)){
                sApetidionDe=this.aPeticionDe.id.toString();
            }            
            
            if(dataAltaFin!=null && !"".equals(dataAltaFin)){
                sDataCelebracionFin=Tools.dateToDateDataBaseFormat(dataAltaFin);
            }
                                
            	    
            reportParams.put("TIPOPUNTOASEMBLEA_ID", sTipoPuntoAsemblea);      
            reportParams.put("SOACORDOS", sSoAcordos);      
            reportParams.put("APETICIONDE_ID", sApetidionDe);
	    reportParams.put("ORGANISMO_ID", org.id.toString());
            reportParams.put("ASEMBLEA_ID", sAsemblea);
	    reportParams.put("DATACELEBRACIONINI", sDataCelebracionIni);
	    reportParams.put("DATACELEBRACIONFIN", sDataCelebracionFin);
            reportParams.put("STITULO",play.i18n.Messages.get("informe.acordos.tituloacordo"));
            reportParams.put("SSUBTITULO",getSubtitulo(dataAltaIni, dataAltaFin));
            reportParams.put("SPUNTO",play.i18n.Messages.get("informe.acordos.punto"));
            reportParams.put("SACORDO",play.i18n.Messages.get("informe.acordos.acordo"));
            reportParams.put("SDATACELEBRACION",play.i18n.Messages.get("informe.acordos.dataCelebracion"));
            reportParams.put("STIPOPUNTOASEMBLEA",play.i18n.Messages.get("informe.acordos.tipoPuntoAsemblea"));
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SAPARTADOCORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
               	    	       
            LoadPropertiesFiles lp=new LoadPropertiesFiles();
                    
            
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String filtroDataAlta(String dataInicial,String dataFinal){
         String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.acordos.subtituloDendeADataRealizacion",dataInicial)+". ";
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.acordos.subtituloAtaADataRealizacion",dataFinal)+". ";
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.acordos.subtituloEntreDataRealizacion",dataInicial,dataFinal)+". ";
         }
         
         return res;
    }
    
 
    
    private String getSubtitulo(String dataAltaIni,String dataAltaFin){
       String res="";
       
      
           res=res + filtroDataAlta(dataAltaIni, dataAltaFin);      
       
       if(this.tipoPuntoAsemblea!=null && !"".equals(this.tipoPuntoAsemblea.toString())){ 
            res=res+play.i18n.Messages.get("informe.acordos.tipoPuntoAsemblea")+": "+tipoPuntoAsemblea+".";
         }
       
       if(this.soConAcordos){
           res=res+play.i18n.Messages.get("informe.acordos.soAcordos")+".";
       }
       
         
         return res;
    }
    

    public String getNomeInforme() {
        return play.i18n.Messages.get("informe.acordos.nome");
    }

    public String getInforme() {
        return "acordos";
    }
    
     public String getFormato() {
        return "pdf";
    }
    
    
    
 
}
