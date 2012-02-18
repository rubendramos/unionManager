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

	public class BaseJasperReports {
	  //here should be jrxml and jasper files, 
	  //you can generate them with iReports(I'm using netbeans plugin)
	  // http://jasperforge.org/projects/ireport 
	  static String REPORT_DEFINITION_PATH = "./app/reports/";

	  public static JasperPrint generateReport(String reportDefFile, Map reportParams) {	   
		JasperPrint jrprint = null;
	    try {
	      String compiledFile = REPORT_DEFINITION_PATH + reportDefFile + ".jasper";
	      JasperCompileManager.compileReportToFile(REPORT_DEFINITION_PATH + reportDefFile + ".jrxml", compiledFile);
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
