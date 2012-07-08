package models.informes;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Informe;
import models.LibroConta;
import models.Organismo;
import models.UnionModel;

import play.db.jpa.*;
import play.mvc.Scope.Params;
import utils.Tools;

 
 
@Entity
public class InformeTesoureria extends Informe{
    
     @ManyToOne
    public LibroConta libroConta;   
    public Date dataApuntamento;
    
   
    

  
    public InformeTesoureria(LibroConta libroConta,Date dataApuntamento) {
    	this.libroConta=libroConta;        
        this.dataApuntamento=dataApuntamento;       
       
    }
      
    public String toString() {
        return this.libroConta==null ?"":" "+this.libroConta.toString();
    }

    public InputStream xeneraInforme(Organismo org,Params params) {
         
        Map reportParams = new HashMap();   
        InformeTesoureria informe=this;
            String dataIni="-1";
            String dataFin="-1";
            String libro="-1";
            
            String dataInicial= params.get("object.dataApuntamento.ini");
            String dataFinal=params.get("object.dataApuntamento.fin");

                                   
            if(informe.libroConta!=null){
                libro=informe.libroConta.id.toString();
            }
            
            if(dataInicial!=null && !"".equals(dataInicial)){
                dataIni=Tools.dateToDateDataBaseFormat(dataInicial);
            }
            
            if(dataFinal!=null && !"".equals(dataFinal)){
                dataFin=Tools.dateToDateDataBaseFormat(dataFinal);
            }
            
            
            
	    
            reportParams.put("LIBRO_ID", libro);
	    reportParams.put("ORGANISMO_ID", org.id.toString());
	    reportParams.put("DATAINI", dataIni);
	    reportParams.put("DATAFIN", dataFin);
            reportParams.put("TITULO",play.i18n.Messages.get("informe.tesoureria.titulo"));
            reportParams.put("SUBTITULO",getSubtitulo(dataInicial, dataFinal));
            
            reportParams.put("STOTAL",play.i18n.Messages.get("informe.tesoureria.total"));            
            reportParams.put("SIMPORTE",play.i18n.Messages.get("informe.tesoureria.importe"));
            reportParams.put("STIPO",play.i18n.Messages.get("informe.tesoureria.tipo"));
            reportParams.put("SCONCEPTO",play.i18n.Messages.get("informe.tesoureria.concepto"));
            reportParams.put("SDESCRICION",play.i18n.Messages.get("informe.tesoureria.descricion"));
            reportParams.put("SDATA",play.i18n.Messages.get("informe.tesoureria.data"));
            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
               	    	       
	    return utils.BaseJasperReports.download_pdf(getInforme(),reportParams);
            
    }
    
    private String getSubtitulo(String dataInicial,String dataFinal){
        String res="";
         if(dataInicial!=null && !"".equals(dataInicial)){ 
            res=play.i18n.Messages.get("informe.tesoureria.subtituloDendeAData",dataInicial);
         }
         if(dataFinal!=null && !"".equals(dataFinal)){
            res=play.i18n.Messages.get("informe.tesoureria.subtituloAtaAData",dataFinal);
         }
         if((dataFinal!=null && !"".equals(dataFinal)) && (dataInicial!=null && !"".equals(dataInicial))){
            res=play.i18n.Messages.get("informe.tesoureria.subtituloEntreDatas",dataInicial,dataFinal);
         }
         
         return res;
    }
    

    public String getNomeInforme() {
        return play.i18n.Messages.get("informe.tesoureria.nome")+ (this.libroConta==null ?"":" "+this.libroConta.toString());
    }

    public String getInforme() {
        return "librosdecontas";
    }
    
     public String getFormato() {
        return "pdf";
    }
    
    
    
 
}
