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
import utils.NewForeignKey;
import utils.Tools;

@Entity
public class Evento extends Model {

    @ManyToOne
    @Required
    public TipoEvento tipoEvento;
    
    @Required
    @MaxSize(50)
    public String nome;

    @Required
    @Lob
    @MaxSize(500)
    public String descricion;
    
    @ManyToOne
    public Conflito conflito;
    
    @ManyToOne
    @Required
    @AddForeignKey
    public Enderezo lugar;
    
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
            String horaRealizacion, String valoracion, Conflito conflito,Set<Documento> documentacion) {
        this.tipoEvento = tipoEvento;
        this.lugar = lugar;
        this.nome = nome;
        this.descricion = descricion;
        this.dataRealizacion = dataRealizacion;
        this.horaRealizacion = horaRealizacion;
        this.conflito = conflito;
        this.valoracion = valoracion;
        this.documentacion=documentacion;
        
    }

    
    public void setLugar(Enderezo enderezo){
        this.lugar=enderezo;
    }
    
    public String toString() {
        
        DateFormat dt= DateFormat.getDateInstance();
        return this.tipoEvento.descricion + " " + this.nome + " " + Tools.getLocaleDateFormat(this.dataRealizacion);
    }
}