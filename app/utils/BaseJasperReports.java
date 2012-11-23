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
import play.Play;

	public class BaseJasperReports {
	
	  static String REPORT_DEFINITION_PATH = Play.configuration.getProperty("repotrs.path");
          static String ATTACHMENT_PATH = Play.configuration.getProperty("attachments.path");

	  public static JasperPrint generateReport(String reportDefFile, Map reportParams) {	   
		JasperPrint jrprint = null;
	    try {
                                    
              InputStream compiledFile=VirtualFile.fromRelativePath(REPORT_DEFINITION_PATH + reportDefFile + ".jasper").inputstream();
                            
              //Engadimos como parametro o path as imaxes que se usan nos repotrs. Nos report temos que engadir este 
              //parámetro na ruta as imaxes.
              String absolute_report_path=play.vfs.VirtualFile.fromRelativePath(REPORT_DEFINITION_PATH).getRealFile().getAbsolutePath();
              String absolute_attachment_path=play.vfs.VirtualFile.fromRelativePath(ATTACHMENT_PATH).getRealFile().getAbsolutePath();
              play.vfs.VirtualFile.fromRelativePath(reportDefFile).getRealFile().getAbsolutePath();
              reportParams.put("REPORTS_DIR", absolute_report_path);
              reportParams.put("SUBREPORTS_DIR", absolute_report_path);
//              reportParams.put("ATTACHMENT_DIR", "C:/Documents and Settings/T00159/Escritorio/softRuben/unionManager/data/attachments");
              reportParams.put("ATTACHMENT_DIR", absolute_attachment_path);
              
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
