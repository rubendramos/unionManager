package models;
 
import controllers.CRUD.Hidden;
import controllers.Seguridade;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.NewForeignKey;
import utils.Tools;
 
@Entity
public class Asemblea extends UnionSecureModel implements Avisable {
 
    
    @Required    
    public String titulo ;
    
    @Required
    @ManyToOne    
    public Afiliado convocante ;
 
    @Required
    @ManyToMany    
    @AddForeignKey
    @NewForeignKey
    public Set<PuntoAsemblea> ordeDoDia ;
       
    @Required
    @AddFiltro
    public Date dataCelebracion;
    
    @Required
    @MaxSize(5)
    public String horaCelebracion;
    
    @Required 
    @ManyToOne
    @AddForeignKey
    public Enderezo lugarCelebracion;

    
    @ManyToMany
    @AddForeignKey
    @NewForeignKey
    public Set<Documento> documentacionAsemblea;
    
    
   
  
    
    public Asemblea(Afiliado convocante,Set<PuntoAsemblea> ordeDoDia,Date dataCelebracion, 
            Enderezo lugarCelebracion, Set<Documento> documentacionAsemblea,String horaCelebracion){
    	this.convocante=convocante;
    	this.ordeDoDia=ordeDoDia;
    	this.dataCelebracion=dataCelebracion;
    	this.lugarCelebracion=lugarCelebracion;
    	this.documentacionAsemblea=documentacionAsemblea;
        this.horaCelebracion=horaCelebracion;
        
    	
    }

    
    
    public String toString() {
        return "Asemblea:  " +this.titulo+" do "+ Tools.getLocaleDateFormat(this.dataCelebracion);
    }

    
      public  Documento creaConvocaAsemblea(Organismo organismo){
        TipoDocumento td=TipoDocumento.findById(Long.parseLong("1"));
        Documento newDoc=null;
        Map reportParams = new HashMap();
        reportParams.put("ASAMBLEA_ID", this.id.toString());
        reportParams.put("SSAUDO", play.i18n.Messages.get("informe.asemblea.saudo"));                     
        reportParams.put("SSECRETARIA", play.i18n.Messages.get("informe.asemblea.secretaria"));
        reportParams.put("SCONVOCATORIA", play.i18n.Messages.get("informe.asemblea.convocatoria",this.titulo,Tools.getLocaleDateFormat(this.dataCelebracion),this.horaCelebracion));
	reportParams.put("SCARNECONF", play.i18n.Messages.get("informe.asemblea.carneconf"));
        reportParams.put("SORDEDIA", play.i18n.Messages.get("informe.asemblea.ordedia"));
        
        reportParams.put("STELEFONO",play.i18n.Messages.get("informe.generic.telefono"));
        reportParams.put("SFAX",play.i18n.Messages.get("informe.generic.fax"));
        reportParams.put("SACORREOS",play.i18n.Messages.get("informe.generic.apartadoCorreos"));
        reportParams.put("SEMAIL",play.i18n.Messages.get("informe.generic.email"));
        
        
        String name= "Convocatoria "+this.titulo+ Tools.getLocaleDateFormat(this.dataCelebracion);     
        Blob b=new Blob();
        Documento doc=Documento.find("byNome",name).first();
        if(doc!=null){
            b.set(utils.BaseJasperReports.download_pdf("asemblea",reportParams), "application/pdf");            
            doc.setFicheiro(b);
            doc._save();
            return doc; 
            
            

        } else{
            b.set(utils.BaseJasperReports.download_pdf("asemblea",reportParams), "application/pdf");            
            newDoc=new Documento(td,"Convocatoria "+this.titulo+Tools.getLocaleDateFormat(dataCelebracion),this.titulo,this.dataCelebracion, this.convocante.persoa.nomeCompleto, b);
            
            newDoc.organismo=organismo;
            newDoc._save();  
            return newDoc; 
        }
                  

   
    }

    public String getAsunto() {
        return play.i18n.Messages.get("aviso.asemblea.asunto", this.titulo);
    }

    public Set<ListaDistribucion> getContactos() {
        HashSet<ListaDistribucion> contactos=new HashSet<ListaDistribucion>();
        ListaDistribucion ld=(ListaDistribucion)ListaDistribucion.getListaAutomaticaAfiliados(organismo);
        contactos.add(ld);
        return contactos;
    }

    public Set<Documento> getAdxuntos() {
        return this.documentacionAsemblea;
    }

    public String getContido() {
        return play.i18n.Messages.get("aviso.asemblea.contido",Tools.getLocaleDateFormat(this.dataCelebracion), this.horaCelebracion, this.lugarCelebracion);
    }
}
