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
public class InformeConflitos extends Informe{
    
    @ManyToOne
    public TipoConflito tipoConflito;   
       
    public Date dataAlta;
    public Date dataBaixa;
    
    

  
    public InformeConflitos(TipoConflito tipoConflito,Date dataAlta,Date dataBaixa) {
    	this.tipoConflito=tipoConflito;         
        this.dataBaixa=dataBaixa;       
        this.dataAlta=dataAlta;
       
       
    }
      
    public String toString() {
        return getNomeInforme();
    }

    public InputStream xeneraInforme(Organismo org,Params params) {
         
        Map reportParams = new HashMap();           
            String sDataAltaIni="-1";
            String sDataBaixaIni="-1";
            String sDataAltaFin="-1";
            String sDataBaixaFin="-1";
            String sTipoConflito="-1";
            
            
            
            
            String dataAltaIni= params.get("object.dataAlta.ini");
            String dataAltaFin=params.get("object.dataAlta.fin");

            String dataBaixaIni= params.get("object.dataBaixa.ini");
            String dataBaixaFin=params.get("object.dataBaixa.fin");
                                   
            if(this.tipoConflito!=null){
                sTipoConflito=this.tipoConflito.id.toString();
            }

     
            if(dataAltaIni!=null && !"".equals(dataAltaIni)){
                sDataAltaIni=Tools.dateToDateDataBaseFormat(dataAltaIni);
            }
            
            if(dataAltaFin!=null && !"".equals(dataAltaFin)){
                sDataAltaFin=Tools.dateToDateDataBaseFormat(dataAltaFin);
            }
            
            if(dataBaixaIni!=null && !"".equals(dataBaixaIni)){
                sDataBaixaIni=Tools.dateToDateDataBaseFormat(dataBaixaIni);
            }
            
            if(dataBaixaFin!=null && !"".equals(dataBaixaFin)){
                sDataBaixaFin=Tools.dateToDateDataBaseFormat(dataBaixaFin);
            }            
            
            
            
	    
            reportParams.put("TIPOCONFLITO_ID", sTipoConflito);                     
	    reportParams.put("ORGANISMO_ID", org.id.toString());
	    reportParams.put("DATAALTAINI", sDataAltaIni);
	    reportParams.put("DATAALTAFIN", sDataAltaFin);
	    reportParams.put("DATABAIXAINI", sDataBaixaIni);
	    reportParams.put("DATABAIXAFIN", sDataBaixaFin);
            reportParams.put("TITULO",play.i18n.Messages.get("informe.conflitos.titulo"));
            reportParams.put("SUBTITULO",getSubtitulo(dataAltaIni, dataAltaFin,dataBaixaIni, dataBaixaFin));
            
            reportParams.put("SDATAINICIO",play.i18n.Messages.get("informe.conflitos.datainicio"));
            reportParams.put("SDATAREMATE",play.i18n.Messages.get("informe.conflitos.dataremate"));
            reportParams.put("STIPOCONFLITO",play.i18n.Messages.get("informe.conflitos.tipoconflito"));
            reportParams.put("SDESCRICIONCONFLITO",play.i18n.Messages.get("informe.conflitos.descricionconflito"));
            reportParams.put("SNOMECONFLITO",play.i18n.Messages.get("informe.conflitos.nomeconflito"));

            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
               	    	       
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String filtroDataAlta(String dataInicial,String dataFinal){
         String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.conflitos.subtituloDendeADataAlta",dataInicial)+". ";
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.conflitos.subtituloAtaADataAlta",dataFinal)+". ";
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.conflitos.subtituloEntreDataAlta",dataInicial,dataFinal)+". ";
         }
         
         return res;
    }
    
  private String filtroDataBaixa(String dataInicial,String dataFinal){
         String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.conflitos.subtituloDendeADataBaixa",dataInicial)+". ";
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.conflitos.subtituloAtaADataBaixa",dataFinal)+". ";
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.conflitos.subtituloEntreDataBaixa",dataInicial,dataFinal)+". ";
         }
         
         return res;
    }    
    
    private String getSubtitulo(String dataAltaIni,String dataAltaFin,String dataBaixaIni,String dataBaixaFin){
       String res="";
       
      
           res=res + filtroDataAlta(dataAltaIni, dataAltaFin);
      
       
      
           res=res + filtroDataBaixa(dataBaixaIni, dataBaixaFin);

       
       if(this.tipoConflito!=null && !"".equals(this.tipoConflito.toString())){ 
            res=res+play.i18n.Messages.get("informe.conflitos.tipoconflito")+": "+tipoConflito+".";
         }
       
         
         return res;
    }
    

    public String getNomeInforme() {
        return play.i18n.Messages.get("informe.conflitos.nome");
    }

    public String getInforme() {
        return "conflitos";
    }
    
     public String getFormato() {
        return "pdf";
    }
    
    
    
 
}
