package models;

import controllers.CRUD;
import java.util.*;
import javax.persistence.*;
import play.data.validation.Required;
import play.data.validation.MaxSize;

import play.db.jpa.*;
import utils.AddFiltro;
import utils.AddForeignKey;

@Entity
public class Aviso extends UnionModel {

    @Required
    @ManyToMany
    @AddForeignKey
    public Set<ListaDistribucion> contactos;
    
    @Required 
    public String asunto;
    
    
    @CRUD.Hidden
    @ManyToOne
    public Evento evento;
    
    @CRUD.Hidden
    @ManyToOne
    public Asemblea asemblea;    
    
    @Lob
    @MaxSize(500)
    private String contido;
    
    @ManyToMany
    @AddForeignKey
    public Set<Documento> docsAdxuntos;
    
    @ManyToOne
    @Required
    @AddFiltro
    private TipoEstadoAviso estadoAviso;
    
    @AddFiltro
    @CRUD.Exclude
    private Date dataRealizacionAviso;
    
    public Date dataARealizarAviso;
        

    public Aviso(){};
    
    public Aviso(Evento evento,Asemblea asemblea,String asunto,String contido,Set<Documento> docsAdxuntos, Set<ListaDistribucion> contactos, 
    		TipoEstadoAviso estadoAviso,Date dataRealizacionAviso,Date dataARealizarAviso){
    	this.asemblea=asemblea;
        this.evento=evento;
    	this.asunto=asunto;
    	this.contido=contido;
    	this.docsAdxuntos=docsAdxuntos;
    	this.contactos=contactos;
    	this.estadoAviso=estadoAviso;
        this.dataARealizarAviso=dataARealizarAviso;
        this.dataRealizacionAviso=dataRealizacionAviso;
      
    	
    }
    
    public String toString(){
        
        if(evento!=null){
            return evento.toString()+"-"+ this.getEstadoAviso().descricion+""+this.getDataRealizacionAviso() ;
        }else if(asemblea!=null){
           return asemblea.toString()+"-"+ this.getEstadoAviso().descricion+""+this.getDataRealizacionAviso() ;
        } else{
            return this.getAsunto()+ "-"+ this.getEstadoAviso().descricion+"-"+ this.getDataRealizacionAviso();
        }
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
        if(evento!=null){
            this.contido=this.evento.nome;
        }else if (asemblea!=null){
            this.contido = this.asemblea.titulo;
        }else {
            this.contido = contido;
        }
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

}