package controllers;

import java.util.HashMap;
import java.util.Map;
import play.*;
import play.mvc.*;

@Check("admin")
@With(Secure.class)
public class Organismos extends CRUD {    
    
 public static void xeneraFichaOrganismo(String idOrganismo,String page, String search) {
         
        Map reportParams = new HashMap();           
            
            if (idOrganismo==null || "".equals(idOrganismo)){
                idOrganismo="-1";
            }
            
            String nomeInforme="fichaOrganismo";
            
	    reportParams.put("ORGANISMO_ID",idOrganismo);           
            reportParams.put("STITULO",play.i18n.Messages.get("informe.fichaOrganismo.titulo"));           
            reportParams.put("SCIF",play.i18n.Messages.get("informe.fichaOrganismo.cif"));            
            reportParams.put("SNOME",play.i18n.Messages.get("informe.fichaOrganismo.nomeSindicato"));
            reportParams.put("SACRONIMO",play.i18n.Messages.get("informe.fichaOrganismo.acronimoSindicato"));
            reportParams.put("SDATAALTA",play.i18n.Messages.get("informe.fichaOrganismo.dataalta"));
            reportParams.put("SDATABAXIA",play.i18n.Messages.get("informe.fichaOrganismo.databaixa"));
            reportParams.put("SNOMEAFILIADO",play.i18n.Messages.get("informe.fichaOrganismo.nomeAfiliados"));
            reportParams.put("STITCULOCOMPONENTES",play.i18n.Messages.get("informe.fichaOrganismo.titulocomponentes"));
                    reportParams.put("STITCOMITE",play.i18n.Messages.get("informe.fichaOrganismo.titComites"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.fichaOrganismo.eMail"));
            reportParams.put("SWEB",play.i18n.Messages.get("informe.fichaOrganismo.web"));
            
            reportParams.put("SENDEREZO",play.i18n.Messages.get("informe.generic.enderezo"));
            reportParams.put("SLOCALIDADE",play.i18n.Messages.get("informe.generic.localidade"));
            reportParams.put("SCODIGOPOSTAL",play.i18n.Messages.get("informe.generic.codigoPostal"));
            reportParams.put("SCONCELLO",play.i18n.Messages.get("informe.generic.concello"));
            reportParams.put("SPROVINCIA",play.i18n.Messages.get("informe.generic.provincia"));
            reportParams.put("SCOMUNIDADE",play.i18n.Messages.get("informe.generic.comunidade"));
            
            
            reportParams.put("SNOMESINDICATO",play.i18n.Messages.get("informe.fichaOrganismo.sindicato"));
            
            
            reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
            reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
            reportParams.put("SAPARTADOCORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
            reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
            reportParams.put("SSECRETARIA",play.i18n.Messages.get("informe.fichaOrganismo.secretaria"));
            reportParams.put("TITULOENDEREZOORGANISMI",play.i18n.Messages.get("informe.fichaOrganismo.tituloEnderezo"));
            reportParams.put("SDATOSORGANISMO",play.i18n.Messages.get("informe.fichaOrganismo.datosOrganismo"));
            reportParams.put("SDESCRICION",play.i18n.Messages.get("informe.fichaOrganismo.descricion"));
            
            
            
	    renderBinary(utils.BaseJasperReports.download_pdf(nomeInforme,reportParams),nomeInforme+".pdf");
            
    }     
}