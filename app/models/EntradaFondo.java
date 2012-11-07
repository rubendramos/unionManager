package models;
 
import controllers.CRUD;
import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.apache.log4j.pattern.IntegerPatternConverter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import utils.AddForeignKey;

 
import play.db.jpa.*;
import play.data.validation.*;
import utils.*;
 
@Entity
@FilterDef(name="entradasEnVenda")
@Filter(name="entradasEnVenda", condition="((estaEnVenda = 'TRUE'))" )


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
    public int anoEdicion;
           
    public boolean ePrestable;
    
    
    
    @Required
    @MaxSize(2)
    public int nExemplares;
    
    @Required
    @MaxSize(2)
    public int nExemplaresLectura;
   
    @Required
    @MaxSize(2)
    public int nExemplaresPrestamo;

    @CRUD.Hidden    
    private int nExemplaresPrestados;
    
    
    public boolean estaEnVenda;
    
    @Required
    @MaxSize(3)
    public int nExemplaresEnVenda;
    
    @CRUD.Hidden    
    private int nExemplaresVendidos;
    
    
    @PlayCurrency
    public Double importe;
    
    public Double descontoAfiliados;
    
    public Blob caratula;  
  
    public EntradaFondo(TipoEntradaFondo tipoEntradaFondo,TipoGeneroFondo tipoGeneroFondo,String titulo, 
            int anoEdicion, String autor,boolean ePrestable,String descricion,Blob caratula,
            String sinatura,int nExemplares,int nExemplaresLectura,int nExemplaresPrestamo,int nExemplaresPrestados,boolean estaEnVenda,int nExemplaresEnVenda,
            int nExemplaresVendidos,Double importe,Double descontoAfiliados){
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
        this.estaEnVenda=estaEnVenda;
        this.nExemplaresEnVenda=nExemplaresEnVenda;
        this.nExemplaresVendidos=nExemplaresVendidos;   
        this.importe=importe;
        this.descontoAfiliados=descontoAfiliados;        
    }

    
    public String toString() {
        return this.tipoEntradaFondo.toString()+"-"+this.tipoGeneroFondo.toString()+" - "+ this.titulo;
    }
 
    public boolean estaDisponibleParaPrestamo(){
        int iPrestados=getnExemplaresPrestados();
        int iPrestamo=nExemplaresPrestamo;
        
        return (this.ePrestable && iPrestados<iPrestamo)?true:false;
    }
    
    public boolean estaDisponibleParaVenda(){
        int iVendidos=getnExemplaresVendidos();
        int iAVenda=nExemplaresEnVenda;
        
        return (this.estaEnVenda && iVendidos<iAVenda)?true:false;
    }    
    
    public boolean estaDisponibleParaLectura(){
        int iExemplaresLectura=this.nExemplaresLectura;        
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
    
    public int getnExemplaresPrestados(){
        return (Integer.valueOf(this.nExemplaresPrestados)==null || this.nExemplaresPrestados==0) ? 0 : this.nExemplaresPrestados;        
    }

    /**
     * @param nExemplaresPrestados the nExemplaresPrestados to set
     */
    public void setnExemplaresPrestados(int nExemplaresPrestados) {
        this.nExemplaresPrestados = nExemplaresPrestados;
    }
    
    public  String exemplaresDisponiblePrestamo() {
        int iExemplaresPrestados=this.nExemplaresPrestados;
        int iExemplaresParaPrestar=this.nExemplaresPrestamo;
        int res=iExemplaresParaPrestar-iExemplaresPrestados;
        return Integer.toString(res);
    }
    
    public  String exemplaresDisponibleVenda() {
        int iExemplaresEnVenta=this.nExemplaresEnVenda;
        int iExemplaresVendidos=this.nExemplaresVendidos;
        int res=iExemplaresEnVenta-iExemplaresVendidos;
        return Integer.toString(res);
    }    

    /**
     * @return the nExemplaresVendidos
     */
    public int getnExemplaresVendidos() {
        return nExemplaresVendidos;
    }

    /**
     * @param nExemplaresVendidos the nExemplaresVendidos to set
     */
    public void setnExemplaresVendidos(int nExemplaresVendidos) {
        this.nExemplaresVendidos = nExemplaresVendidos;
    }
    

}
