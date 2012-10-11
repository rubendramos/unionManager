package models;
 
import controllers.CRUD.Hidden;
import java.io.File;
import java.util.*;
import javax.persistence.*;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.Tools;
 
@Entity
public class Documento extends UnionSecureModel {
 
    
    @Required
    @ManyToOne    
    @AddFiltro
    public TipoDocumento tipoDocumento ;
    
    @Required    
    @MaxSize(100)
    public String nome;

    @Lob
    @MaxSize(500)
    public String descricion;
    
    
    @Required
    @AddFiltro
    public Date dataCreacion;
    
    @MaxSize(100)
    public String autor ;
    
    public Blob ficheiro;
    
    @Hidden
    private String nomeFicheiro;
  
    public Documento(TipoDocumento tipoDocumento,String nome,String descricion,Date dataCreacion,
            String autor, Blob ficheiro){
    	this.tipoDocumento=tipoDocumento;
    	this.nome=nome;
        this.dataCreacion=dataCreacion;
        this.autor=autor;
        this.descricion=descricion;
        this.ficheiro=ficheiro;
        this.nomeFicheiro=ficheiro.getFile().getName();
    	
    }

    
    
    public String toString() {
        return this.tipoDocumento+""+ this.nome+" "+Tools.getLocaleDateFormat(this.dataCreacion);
    }

    /**
     * @return the ficheiro
     */
    public Blob getFicheiro() {
        return ficheiro;
    }

    /**
     * @param ficheiro the ficheiro to set
     */
    public void setFicheiro(Blob ficheiro) {
        this.ficheiro = ficheiro;
    }

    /**
     * @return the nomeFicheiro
     */
    public String getNomeFicheiro() {
        return nomeFicheiro;
    }

    /**
     * @param nomeFicheiro the nomeFicheiro to set
     */
    public void setNomeFicheiro(String nomeFicheiro) {
        this.nomeFicheiro = getFicheiro().getFile().getName();
    }

   
    
 
 
}