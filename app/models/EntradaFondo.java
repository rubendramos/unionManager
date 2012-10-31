package models;
 
import controllers.CRUD;
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.apache.log4j.pattern.IntegerPatternConverter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.NewForeignKey;
import utils.Tools;
 
@Entity
@FilterDef(name="entradasPrestables", defaultCondition="((eprestable = 'TRUE'))" )
@Filter(name="entradasPrestables")
public class EntradaFondo extends UnionModel {
 

    @Required
    @ManyToOne
    @AddFiltro
    public Fondo fondo;
    
    
    @Required
    @ManyToOne
    @AddFiltro
    public TipoEntradaFondo tipoEntradaFondo;
       
    @Required
    @ManyToOne
    @AddFiltro
    public TipoGeneroFondo tipoGeneroFondo;
    
    @Required
    @MaxSize(10)
    public String sinatura;
    
    @Required
    @MaxSize(100)
    public String titulo;
    
    @MaxSize(500)
    public String descricion;
 
    @Required
    @MaxSize(300)    
    @Lob
    public String autor;    
    
    @Required
    @MaxSize(4)
    public String anoEdicion;
           
    public boolean ePrestable;
    
    public Blob caratula;  
    
    @Required
    @MaxSize(2)
    public String nExemplares;
    
    @Required
    @MaxSize(2)
    public String nExemplaresLectura;
   
    @Required
    @MaxSize(2)
    public String nExemplaresPrestamo;

    @CRUD.Hidden    
    private String nExemplaresPrestados;
  
    public EntradaFondo(TipoEntradaFondo tipoEntradaFondo,TipoGeneroFondo tipoGeneroFondo,String titulo, 
            String anoEdicion, String autor,boolean ePrestable,String descricion,Blob caratula,
            String sinatura,String nExemplares,String nExemplaresLectura,String nExemplaresPrestamo,String nExemplaresPrestados){
    	this.tipoEntradaFondo=tipoEntradaFondo;
    	this.tipoGeneroFondo=tipoGeneroFondo;
    	this.titulo=titulo;
    	this.anoEdicion=anoEdicion;
    	this.autor=autor;
    	this.caratula= caratula;
    	this.descricion=descricion;
        this.ePrestable=ePrestable;
        this.sinatura=sinatura;
        this.nExemplares=nExemplares;
        this.nExemplaresLectura=nExemplaresLectura;
        this.nExemplaresPrestados=nExemplaresPrestados;
        this.nExemplaresPrestamo=nExemplaresPrestamo;
        
    }

    
    public String toString() {
        return this.tipoEntradaFondo.toString()+"-"+this.tipoGeneroFondo.toString()+" - "+ this.titulo;
    }
 
    public boolean estaDisponibleParaPrestamo(){
        int iPrestados=Integer.parseInt(getnExemplaresPrestados());
        int iPrestamo=Integer.parseInt(nExemplaresPrestamo);
        
        return (this.ePrestable && iPrestados<iPrestamo)?true:false;
    }
    
    public boolean estaDisponibleParaLectura(){
        int iExemplaresLectura=Integer.parseInt(this.nExemplaresLectura);        
        if(iExemplaresLectura>0){
            return true;
        }else{
            return estaDisponibleParaPrestamo();
        }
    }
    
    public String proximaDataDevolucion(){
        Date dataDev=PrestamoFondo.getDataDevolucionFondo(this);
        
        return dataDev==null?"":Tools.getLocaleDateFormat(dataDev);

    }
    
    public String getnExemplaresPrestados(){
        return (this.nExemplaresPrestados==null || "".equals(this.nExemplaresPrestados)) ?"0" :this.nExemplaresPrestados;        
    }

    /**
     * @param nExemplaresPrestados the nExemplaresPrestados to set
     */
    public void setnExemplaresPrestados(String nExemplaresPrestados) {
        this.nExemplaresPrestados = nExemplaresPrestados;
    }
    
    public  String exemplaresDisponiblePrestamo() {
        int iExemplaresPrestados=Integer.parseInt(this.nExemplaresPrestados);
        int iExemplaresParaPrestar=Integer.parseInt(this.nExemplaresPrestamo);
        int res=iExemplaresParaPrestar-iExemplaresPrestados;
        return Integer.toString(res);
    }    
}
