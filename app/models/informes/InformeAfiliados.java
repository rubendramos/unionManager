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
public class InformeAfiliados extends Informe{
    
    @ManyToOne
    public Ramo ramo;   
    
    @ManyToOne
    public Ocupacion ocupacion;
    
    public Date dataAlta;
    public Date dataBaixa;
    
    

  
    public InformeAfiliados(Ramo ramo,Ocupacion ocupacion,Date dataAlta,Date dataBaixa) {
    	this.ramo=ramo; 
        this.ocupacion=ocupacion;
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
            String sRamo="-1";
            String sOcupacion="-1";
            
            
            
            String dataAltaIni= params.get("object.dataAlta.ini");
            String dataAltaFin=params.get("object.dataAlta.fin");

            String dataBaixaIni= params.get("object.dataBaixa.ini");
            String dataBaixaFin=params.get("object.dataBaixa.fin");
                                   
            if(this.ramo!=null){
                sRamo=this.ramo.id.toString();
            }

            if(this.ocupacion!=null){
                sOcupacion=this.ocupacion.id.toString();
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
            
            
            
	    
            reportParams.put("RAMO_ID", sRamo);           
            reportParams.put("OCUPACION_ID", sOcupacion);
	    reportParams.put("ORGANISMO_ID", org.id.toString());
	    reportParams.put("DATAALTAINI", sDataAltaIni);
	    reportParams.put("DATAALTAFIN", sDataAltaFin);
	    reportParams.put("DATABAIXAINI", sDataBaixaIni);
	    reportParams.put("DATABAIXAFIN", sDataBaixaFin);
            reportParams.put("TITULO",play.i18n.Messages.get("informe.afiliados.titulo"));
            reportParams.put("SUBTITULO",getSubtitulo(dataAltaIni, dataAltaFin,dataBaixaIni, dataBaixaFin));
            
            reportParams.put("SDNI",play.i18n.Messages.get("informe.afiliados.dni"));            
            reportParams.put("SNOME",play.i18n.Messages.get("informe.afiliados.nomePersoa"));
            reportParams.put("SRAMO",play.i18n.Messages.get("informe.afiliados.ramo"));
            reportParams.put("SESTADO",play.i18n.Messages.get("informe.afiliados.estado"));
            reportParams.put("SOCUPACION",play.i18n.Messages.get("informe.afiliados.ocupacion"));
            reportParams.put("SMILITA",play.i18n.Messages.get("informe.afiliados.milita"));
            reportParams.put("SDATAALTA",play.i18n.Messages.get("informe.afiliados.dataalta"));
            reportParams.put("SDATABAIXA",play.i18n.Messages.get("informe.afiliados.databaixa"));
            reportParams.put("SSI",play.i18n.Messages.get("generic.si"));
            reportParams.put("SNO",play.i18n.Messages.get("generic.no"));
            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
               	    	       
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String filtroDataAlta(String dataInicial,String dataFinal){
         String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.afiliados.subtituloDendeADataAlta",dataInicial)+". ";
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.afiliados.subtituloAtaADataAlta",dataFinal)+". ";
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.afiliados.subtituloEntreDataAlta",dataInicial,dataFinal)+". ";
         }
         
         return res;
    }
    
  private String filtroDataBaixa(String dataInicial,String dataFinal){
         String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.afiliados.subtituloDendeADataBaixa",dataInicial)+". ";
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.afiliados.subtituloAtaADataBaixa",dataFinal)+". ";
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.afiliados.subtituloEntreDataBaixa",dataInicial,dataFinal)+". ";
         }
         
         return res;
    }    
    
    private String getSubtitulo(String dataAltaIni,String dataAltaFin,String dataBaixaIni,String dataBaixaFin){
       String res="";
       
      
           res=res + filtroDataAlta(dataAltaIni, dataAltaFin);
      
       
      
           res=res + filtroDataBaixa(dataBaixaIni, dataBaixaFin);

       
       if(this.ramo!=null && !"".equals(this.ramo.toString())){ 
            res=res+play.i18n.Messages.get("informe.afiliados.ramo")+": "+ramo+" - ";
         }
       if(this.ocupacion!=null && !"".equals(this.ocupacion.toString())){ 
            res=res+play.i18n.Messages.get("informe.afiliados.ocupacion")+": "+this.ocupacion.toString();
         }       
       
         
         return res;
    }
    

    public String getNomeInforme() {
        return play.i18n.Messages.get("informe.afiliados.nome");
    }

    public String getInforme() {
        return "afiliados";
    }
    
     public String getFormato() {
        return "pdf";
    }
    
    
    
 
}
