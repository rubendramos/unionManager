package utils;
	import java.io.ByteArrayInputStream;
	import java.io.ByteArrayOutputStream;
	import java.io.InputStream;
	import java.io.OutputStream;
import java.util.HashMap;
	import java.util.Map;
	import net.sf.jasperreports.engine.JasperFillManager;
	import net.sf.jasperreports.engine.JasperPrint;
	import net.sf.jasperreports.engine.export.JExcelApiExporter;
	import net.sf.jasperreports.engine.export.JRPdfExporter;
	import net.sf.jasperreports.engine.JRExporter;
	import net.sf.jasperreports.engine.JRExporterParameter;
	import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import play.vfs.VirtualFile;

	public class BaseJasperReports {
	
	  static String REPORT_DEFINITION_PATH = "/app/reports/";
          static String ATTACHMENT_PATH = "C:/Documents and Settings/T00159/Escritorio/softRuben/unionManager/data/attachments";

	  public static JasperPrint generateReport(String reportDefFile, Map reportParams) {	   
		JasperPrint jrprint = null;
	    try {
                                    
              InputStream compiledFile=VirtualFile.fromRelativePath(REPORT_DEFINITION_PATH + reportDefFile + ".jasper").inputstream();
                            
              //Engadimos como parametro o path as imaxes que se usan nos repotrs. Nos report temos que engadir este 
              //parámetro na ruta as imaxes.
              reportParams.put("REPORTS_DIR", play.vfs.VirtualFile.fromRelativePath(REPORT_DEFINITION_PATH).getName());
              reportParams.put("ATTACHMENT_DIR", "C:/Documents and Settings/T00159/Escritorio/softRuben/unionManager/data/attachments");
              
              jrprint = JasperFillManager.fillReport(compiledFile, reportParams, play.db.DB.getConnection());	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return jrprint;
	  }
	  
	  public static InputStream exportToPdf(JasperPrint jrprint) {
		    OutputStream os = new ByteArrayOutputStream();
		    try {
		      JRExporter exporter = new JRPdfExporter();
		      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jrprint);
		      exporter.exportReport();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
		  }
	  
	  
	  public static InputStream download_pdf(String docName,Map reportParams) {		    		    
		    JasperPrint jrprint=utils.BaseJasperReports.generateReport(docName, reportParams);		    
		    return utils.BaseJasperReports.exportToPdf(jrprint);		    
		  }	
          		          
	}
