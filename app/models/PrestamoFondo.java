package models;

import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;


import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.Tools;

@Entity
public class PrestamoFondo extends UnionModel {

    @Required
    @ManyToOne
    @AddFiltro
    public Afiliado afiliado;
    @Required
    @ManyToOne
    @AddForeignKey
    @AddFiltro
    public EntradaFondo entradaFondo;
    @Required
    public Date dataPrestamo;
    private Date dataDevolucion;

    public PrestamoFondo(Afiliado afiliado, EntradaFondo entradaFondo, Date dataPrestamo, Date dataDevolucion) {
        this.entradaFondo = entradaFondo;
        this.dataPrestamo = dataPrestamo;
        this.dataDevolucion = dataDevolucion;
        this.afiliado = afiliado;
    }

    public String toString() {
        return this.afiliado + "-" + this.entradaFondo.toString() + "-" + this.dataPrestamo + "-" + this.getDataDevolucion();
    }

    public static Date getDataDevolucionFondo(EntradaFondo entradaFondo) {
        String query = "Select pf from PrestamoFondo pf where entradafondo_id = :entradaFondo";
        JPAQuery jpa = find(query).setParameter("entradaFondo", entradaFondo.id);
        PrestamoFondo pf = (PrestamoFondo) jpa.first();
        Fondo f = Fondo.findById(entradaFondo.fondo.id);
        return pf == null ? null : Tools.addDaysToDate(pf.dataPrestamo, Integer.parseInt(f.periodoDiasPrestamo));
    }

    public  Date getDataVencemento(){
        int days=Integer.parseInt(this.entradaFondo.fondo.periodoDiasPrestamo);
        return Tools.addDaysToDate(dataPrestamo, days);    
    }
    
    public boolean estaPrestamoVencido(){
        Date currentDate=Tools.getCurrentDate();
        Date dataVencemento=getDataVencemento();
        return currentDate.compareTo(dataVencemento)==1 ?true:false;
    }
    
    /**
     * @return the dataDevolucion
     */
    public Date getDataDevolucion() {
        return dataDevolucion;
    }

    /**
     * @param dataDevolucion the dataDevolucion to set
     */
    public void setDataDevolucion(Date dataDevolucion) {
        this.dataDevolucion = dataDevolucion;
    }

    public static List<EntradaFondo> getPrestamosVencidosPorAfiliado(Afiliado afiliado) {
        
            String query = "select ef from PrestamoFondo pf "
                + "join pf.entradaFondo ef "
                + "join ef.fondo f " 
                + "where pf.afiliado.id = :afiliadoId  "
                + "and pf.dataDevolucion is null "
                + "and sysdate > (pf.dataPrestamo + (cast(f.periodoDiasPrestamo ,int)+1))";


        JPAQuery jpa = find(query).setParameter("afiliadoId", afiliado.id);       
        return jpa.fetch();
    }

    public static List<EntradaFondo>  getPrestamosActivosPorAfiliadoEFondo(Afiliado afiliado,Fondo fondo) {        
        
        String query = "select ef from PrestamoFondo pf "
                + "join  pf.entradaFondo ef "
                + "join ef.fondo f " 
                + "where pf.afiliado.id = :afiliadoId "
                + "and pf.dataDevolucion  is null "
                + "and f.id= :fondoId";
        
        JPAQuery jpa = find(query).setParameter("afiliadoId", afiliado.id);
        jpa.setParameter("fondoId", fondo.id);
        return jpa.fetch();
        
               
    }    
    
}
