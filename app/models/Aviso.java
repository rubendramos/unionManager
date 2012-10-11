package models;

import controllers.CRUD;
import java.util.*;
import javax.persistence.*;
import play.data.validation.Required;
import play.data.validation.MaxSize;

import play.db.jpa.*;
import utils.AddFiltro;
import utils.AddForeignKey;
import utils.Tools;




@Entity
public class Aviso extends UnionModel {
    
    

    @Required
    @ManyToMany
    @AddForeignKey
    public Set<ListaDistribucion> contactos;
    
    @Required 
    public String asunto;
    
    
    @Lob
    @MaxSize(500)
    public String contido;
    
    @ManyToMany
    @AddForeignKey
    public Set<Documento> docsAdxuntos;
    
    @ManyToOne
    @Required
    @AddFiltro
    public TipoEstadoAviso estadoAviso;
    
    @AddFiltro
    @CRUD.Exclude
    public Date dataRealizacionAviso;
    
    public Date dataARealizarAviso;
       
    @CRUD.Hidden
    @ManyToOne
    public User firma;

    public Aviso(){};
    
    
    public Aviso(Avisable avisable){    	       
    this.asunto=avisable.getAsunto();
    this.contido=avisable.getContido();
    this.docsAdxuntos=new HashSet<Documento>(avisable.getAdxuntos());
    this.contactos=new HashSet<ListaDistribucion>(avisable.getContactos());
    this.estadoAviso=TipoEstadoAviso.findById(Long.parseLong("1"));
    this.dataARealizarAviso=Tools.getCurrentDate();
    this.dataRealizacionAviso=null;
    
      
    	
    } 
    public Aviso(String asunto,String contido,Set<Documento> docsAdxuntos, Set<ListaDistribucion> contactos, 
    		TipoEstadoAviso estadoAviso,Date dataRealizacionAviso,Date dataARealizarAviso,User firma){    	        
    	this.asunto=asunto;
    	this.contido=contido;
    	this.docsAdxuntos=docsAdxuntos;
    	this.contactos=contactos;
    	this.estadoAviso=estadoAviso;
        this.dataARealizarAviso=dataARealizarAviso;
        this.dataRealizacionAviso=dataRealizacionAviso;
        this.firma=firma;
      
    	
    }
    
    public String toString(){
             
            return this.getAsunto()+ "-"+ this.getEstadoAviso().descricion+"-"+ this.getDataRealizacionAviso();
    }
        
    public List<Contacto> getContactosListasDistribucion() {        
        ArrayList res = new ArrayList();

        for (ListaDistribucion lista : this.contactos) {
            for (Contacto contacto : lista.getContactos(this.organismo.id)) {
                res.add(contacto);
            }
        }
        return res;        

    }
    
    /**
     * @return the asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * @param asunto the asunto to set
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * @return the contido
     */
    public String getContido() {
        return contido;
    }

    /**
     * @param contido the contido to set
     */
    public void setContido(String contido) {       
            this.contido = contido;        
    }

    /**
     * @return the estadoAviso
     */
    public TipoEstadoAviso getEstadoAviso() {
        return estadoAviso;
    }

    /**
     * @param estadoAviso the estadoAviso to set
     */
    public void setEstadoAviso(TipoEstadoAviso estadoAviso) {
        this.estadoAviso = estadoAviso;
    }
  
    public static List<Aviso> findAvisosAEnviar() {
        String query = "from Aviso t  where t.estadoAviso=1";        
        return find(query).fetch();
    }
    
        
    /**
     * @return the dataRealizacionAviso
     */
    public Date getDataRealizacionAviso() {
        return dataRealizacionAviso;
    }

    /**
     * @param dataRealizacionAviso the dataRealizacionAviso to set
     */
    public void setDataRealizacionAviso(Date dataRealizacionAviso) {
        this.dataRealizacionAviso = dataRealizacionAviso;
    }

    /**
     * @return the firma
     */
    public User getFirma() {
        return firma;
    }

    /**
     * @param firma the firma to set
     */
    public void setFirma(User firma) {
        this.firma = firma;
    }

}