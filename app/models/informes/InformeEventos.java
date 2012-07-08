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
public class InformeEventos extends Informe{
    
    @ManyToOne
    public TipoEvento tipoEvento;   
       
    public Date dataRealizacion;

    
    

  
    public InformeEventos(TipoEvento tipoEvento,Date dataRealizacion) {
    	this.tipoEvento=tipoEvento;         
        this.dataRealizacion=dataRealizacion;              
    }
      
    public String toString() {
        return getNomeInforme();
    }

    public InputStream xeneraInforme(Organismo org,Params params) {
         
        Map reportParams = new HashMap();           
            String sDataRealizacionIni="-1";
            
            String sDataRealizacionFin="-1";
            String sTipoEvento="-1";
            String sEvento="-1";
            
            
            
            
            String dataAltaIni= params.get("object.dataRealizacion.ini");
            String dataAltaFin=params.get("object.dataRealizacion.fin");

                                   
            if(this.tipoEvento!=null){
                sTipoEvento=this.tipoEvento.id.toString();
            }

     
            if(dataAltaIni!=null && !"".equals(dataAltaIni)){
                sDataRealizacionIni=Tools.dateToDateDataBaseFormat(dataAltaIni);
            }
            
            if(dataAltaFin!=null && !"".equals(dataAltaFin)){
                sDataRealizacionFin=Tools.dateToDateDataBaseFormat(dataAltaFin);
            }
                                
            	    
            reportParams.put("TIPOEVENTO_ID", sTipoEvento);                     
	    reportParams.put("ORGANISMO_ID", org.id.toString());
            reportParams.put("EVENTO_ID", sEvento);
	    reportParams.put("DATAREALIZACIONINI", sDataRealizacionIni);
	    reportParams.put("DATAREALIZACIONFIN", sDataRealizacionFin);
            reportParams.put("STITULOEVENTO",play.i18n.Messages.get("informe.eventos.tituloEvento"));
            reportParams.put("SUBTITULO",getSubtitulo(dataAltaIni, dataAltaFin));
            reportParams.put("SHORAEVENTO",play.i18n.Messages.get("informe.eventos.horaRealizacionEvento"));
            reportParams.put("SDATAREALIZACION",play.i18n.Messages.get("informe.eventos.dataRealizacionEvento"));
            reportParams.put("STIPOEVENTO",play.i18n.Messages.get("informe.eventos.tipoEvento"));
            reportParams.put("SDESCRICIONEVENTO",play.i18n.Messages.get("informe.eventos.descricionEvento"));
            reportParams.put("SNOMEEVENTO",play.i18n.Messages.get("informe.eventos.nomeEvento"));
            reportParams.put("SVALORACIONEVENTO",play.i18n.Messages.get("informe.eventos.valoracionEvento"));
            reportParams.put("SENDEREZO",play.i18n.Messages.get("informe.eventos.enderezo"));
            reportParams.put("SSUBTITULOEVENTO",getSubtitulo(dataAltaIni, dataAltaFin));
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
               	    	       
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String filtroDataAlta(String dataInicial,String dataFinal){
         String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.eventos.subtituloDendeADataRealizacion",dataInicial)+". ";
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.eventos.subtituloAtaADataRealizacion",dataFinal)+". ";
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.eventos.subtituloEntreDataRealizacion",dataInicial,dataFinal)+". ";
         }
         
         return res;
    }
    
 
    
    private String getSubtitulo(String dataAltaIni,String dataAltaFin){
       String res="";
       
      
           res=res + filtroDataAlta(dataAltaIni, dataAltaFin);      
       
       if(this.tipoEvento!=null && !"".equals(this.tipoEvento.toString())){ 
            res=res+play.i18n.Messages.get("informe.eventos.tipoEvento")+": "+tipoEvento+".";
         }
       
         
         return res;
    }
    

    public String getNomeInforme() {
        return play.i18n.Messages.get("informe.eventos.nome");
    }

    public String getInforme() {
        return "eventos";
    }
    
     public String getFormato() {
        return "pdf";
    }
    
    
    
 
}
