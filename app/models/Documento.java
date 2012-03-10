package models;
 
import java.io.File;
import java.util.*;
import javax.persistence.*;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.Tools;
 
@Entity
public class Documento extends Model {
 
    
    @Required
    @ManyToOne    
    public TipoDocumento tipoDocumento ;
    
    @Required    
    @MaxSize(100)
    public String nome;

    @Lob
    @MaxSize(500)
    public String descricion;
    
    
    @Required
    public Date dataCreacion;
    
    @MaxSize(100)
    public String autor ;
    
    public Blob ficheiro;
  
    public Documento(TipoDocumento tipoDocumento,String nome,String descricion,Date dataCreacion,
            String autor, Blob ficheiro){
    	this.tipoDocumento=tipoDocumento;
    	this.nome=nome;
        this.dataCreacion=dataCreacion;
        this.autor=autor;
        this.descricion=descricion;
        this.ficheiro=ficheiro;       
    	
    }

    
    
    public String toString() {
        return this.tipoDocumento+""+ this.nome+" "+Tools.getLocaleDateFormat(this.dataCreacion);
    }
    
 
 
}