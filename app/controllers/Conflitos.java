package controllers;

import java.util.HashMap;
import java.util.Map;
import play.mvc.*;

@Check("accionSindical")
@With(Secure.class)
public class Conflitos extends CRUD {    
    
    
   public static void xeneraFichaConflito(String idConflito,String page, String search) {
         
        Map reportParams = new HashMap();           
            
            if (idConflito==null || "".equals(idConflito)){
                idConflito="-1";
            }
  
            String nomeInforme="fichaConflito";
            
            
            
	    reportParams.put("CONFLITO_ID",idConflito);           
            reportParams.put("STITULODOCUMENTOS",play.i18n.Messages.get("informe.fichaConflito.tituloDocumentos"));           
            reportParams.put("SDESCRICIONDOCUMENTO",play.i18n.Messages.get("informe.fichaConflito.descricionDocumento"));            
            reportParams.put("SNOMEDOCUMENTO",play.i18n.Messages.get("informe.fichaConflito.nomeDocumento"));
            reportParams.put("STIPODOCUMENTO",play.i18n.Messages.get("informe.fichaConflito.tipodocumento"));
            reportParams.put("STITULOAFILIADOSCONFLOTO",play.i18n.Messages.get("informe.fichaConflito.tituloAfiliadosConfloto"));
            reportParams.put("SDATABAIXA",play.i18n.Messages.get("informe.fichaConflito.dataBaixa"));
            reportParams.put("SDATAALTA",play.i18n.Messages.get("informe.fichaConflito.dataAlta"));
            reportParams.put("SNOMEAFILIADO",play.i18n.Messages.get("informe.fichaConflito.nomeAfiliado"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.fichaConflito.eMail"));
            reportParams.put("STIPOEVENTO",play.i18n.Messages.get("informe.fichaConflito.tipoEvento"));
            reportParams.put("SNOMEEVENTO",play.i18n.Messages.get("informe.fichaConflito.nomeEvento"));
            reportParams.put("SDESCRICIONEVENTO",play.i18n.Messages.get("informe.fichaConflito.descricionEvento"));
            reportParams.put("SVALORACIONEVENTO",play.i18n.Messages.get("informe.fichaConflito.valoracionEvento"));
            reportParams.put("SDATAREALIZACION",play.i18n.Messages.get("informe.fichaConflito.dataRealizacionEvento"));
            reportParams.put("SHORAEVENTO",play.i18n.Messages.get("informe.fichaConflito.horaRealizacionEvento"));
            reportParams.put("SENDEREZO",play.i18n.Messages.get("informe.fichaConflito.enderezo"));
            reportParams.put("STITULOEVENTO",play.i18n.Messages.get("informe.fichaConflito.tituloEvento"));
            reportParams.put("STIPOCONFLITO",play.i18n.Messages.get("informe.fichaConflito.tipoConflito"));
            reportParams.put("SNOMECONFLITO",play.i18n.Messages.get("informe.fichaConflito.nomeConflito"));
            reportParams.put("SDESCRICIONCONFLITO",play.i18n.Messages.get("informe.fichaConflito.descricionConflito"));
            
            
            reportParams.put("SDATAREMATE",play.i18n.Messages.get("informe.fichaConflito.dataRemate"));
            reportParams.put("SDATAINICIO",play.i18n.Messages.get("informe.fichaConflito.dataInicio"));
            reportParams.put("SVALORACIONCONFLITO",play.i18n.Messages.get("informe.fichaConflito.valoracionConflito"));
            reportParams.put("STITULOCONFLITO",play.i18n.Messages.get("informe.fichaConflito.tituloConflito"));
            reportParams.put("STITULOINFORME",play.i18n.Messages.get("informe.fichaConflito.tituloFichaConflito"));
            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SAPARTADOCORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));            
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
            
            
	    renderBinary(utils.BaseJasperReports.download_pdf(nomeInforme,reportParams),nomeInforme+".pdf");
            
    } 
    
}
