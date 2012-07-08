package models;

import controllers.CRUD;
import controllers.Seguridade;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import utils.AddForeignKey;
import utils.NewForeignKey;

import java.util.*;
import org.hibernate.annotations.Filter;
import utils.AddFiltro;
import utils.Tools;

@Entity
public class Consulta extends UnionSecureModel implements Notificable{

    @MaxSize(15)
    public String dni;
    
    @Required
    @MaxSize(50)
    public String nome;
    
    @Required
    @MaxSize(50)
    public String apelido1;

    @MaxSize(50)
    public String apelido2;
    
    @ManyToOne
    @Required
    @AddFiltro
    public TipoConflito tipoConflito;
    
    @ManyToOne
    @Required
    @AddFiltro
    public Ramo ramo;

    @ManyToOne
    @Required
    @AddFiltro
    public Ocupacion ocupacion;
      
    @CRUD.Hidden
    @CRUD.Exclude
    public Date dataAlta;

    @CRUD.Hidden
    @CRUD.Exclude
    public Date dataModificacion;
    
    
    
    @Required
    @Lob
    @MaxSize(500)
    public String descricion;               
    
    @ManyToMany
    @AddForeignKey
    @NewForeignKey
    public Set<Documento> documentacion = new HashSet();
    
    
    @Lob
    @MaxSize(500)
    public String resposta;     

    public Consulta(String dni, String nome, String apelido1, String apelido2,TipoConflito tipoConflito, Ramo ramo,
            Date dataAlta, String descricion, Set<Documento> documentacion,Ocupacion ocupacion, Date dataModifiacion,String reposta){
        this.tipoConflito = tipoConflito;
        this.nome = nome;
        this.descricion = descricion;
        this.apelido1 = apelido1;
        this.apelido2 = apelido2;
        this.ramo = ramo;
        this.ocupacion = ocupacion;
        this.dataAlta = dataAlta;
        this.dataModificacion = dataModifiacion;
        this.documentacion = documentacion;
        this.dni = dni;
        this.resposta=resposta;
    }

    public String toString() {
        return this.tipoConflito.descricion + "  " + this.nome + " " + Tools.getLocaleDateFormat(this.dataAlta);
    }

    public Aviso getAviso() {
        Aviso av=new Aviso();
        return av;
       
    }

    public NotificacionInterna getNotificacionInterna() {
        //NotificacionInterna ni=new NotificacionInterna();
        TipoPrioridade tipoPrioridad=TipoPrioridade.findById(Long.parseLong("2"));
        String contido="<a href='consultas/show?id=65'>Foi dada de alta unha nova consulta</a>";
        NotificacionInterna ni=new NotificacionInterna("Notificacion consulta: "+this.tipoConflito.descricion,contido,null,Tools.getCurrentDate(),null,tipoPrioridad);
        return ni;
    }
}
