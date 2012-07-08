package models;

import java.text.DateFormat;
import java.text.NumberFormat;
import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;
import utils.HellperBinder;
import utils.AddForeignKey;
import play.data.binding.As;

import javax.persistence.*;

import java.util.*;
import utils.AddFiltro;
import utils.NewForeignKey;
import utils.Tools;

@Entity
public class Evento extends UnionSecureModel implements Avisable {

    @ManyToOne
    @Required
    @AddFiltro
    public TipoEvento tipoEvento;
    
    @Required
    @MaxSize(50)
    public String nome;

    @Required
    @Lob
    @MaxSize(500)
    public String descricion;
    
    @ManyToOne
    @Required
    @AddForeignKey
    public Enderezo lugar;
    
    @AddFiltro
    public Date dataRealizacion;
    
    @MaxSize(5)
    public String horaRealizacion;
    
    @Lob
    @MaxSize(500)
    public String valoracion;
    
  
    
    @ManyToMany
    @AddForeignKey
    @NewForeignKey
    public Set<Documento> documentacion = new HashSet() ;        

    public Evento(TipoEvento tipoEvento, Enderezo lugar, String nome, String descricion, Date dataRealizacion, 
            String horaRealizacion, String valoracion, Set<Documento> documentacion) {
        this.tipoEvento = tipoEvento;
        this.lugar = lugar;
        this.nome = nome;
        this.descricion = descricion;
        this.dataRealizacion = dataRealizacion;
        this.horaRealizacion = horaRealizacion;        
        this.valoracion = valoracion;
        this.documentacion=documentacion;
        
    }

    
    public void setLugar(Enderezo enderezo){
        this.lugar=enderezo;
    }
    
    public String toString() {
                
        return this.tipoEvento.descricion + " " + this.nome + " " + Tools.getLocaleDateFormat(this.dataRealizacion);
    }

    public Enderezo getLugarCelebracion() {
        return this.lugar;
    }

    public Set<Documento> getAdxuntos() {
        return this.documentacion;
    }

    public String getHoraCelebracion() {
        return this.horaRealizacion;
    }

    public String getTitulo() {
        return this.nome;
    }

}