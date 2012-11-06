package models;

import controllers.CRUD.Hidden;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Filter;
import utils.AddForeignKey;


import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;
import utils.PlayCurrency;
import utils.Tools;

@Entity
public class VendaFondo extends UnionModel {

    
    @ManyToOne
    @AddFiltro
    public Afiliado afiliado;
    
    @Required
    @ManyToOne
    @AddForeignKey
    @AddFiltro
    public EntradaFondo entradaFondo;
    
    @Required
    public Date dataVenda;
    
    @Required
    public Date dataDevolucionVenda;
    
    @Required
    public String nUnidades;
    
    @Required
    @PlayCurrency
    public Double importeVenda;

    public VendaFondo(Afiliado afiliado, EntradaFondo entradaFondo, Date dataVenda,
            Double importeVenda,String nUnidades,Date dataDevolucionVenda) {
        this.entradaFondo = entradaFondo;
        this.dataVenda = dataVenda;
        this.nUnidades = nUnidades;
        this.afiliado = afiliado;
        this.importeVenda=importeVenda;
        this.dataDevolucionVenda=dataDevolucionVenda;
    }

    public String toString() {
        return this.entradaFondo.toString() + "-" + this.nUnidades + "-";
    }
    
    /**
     * @return the dataDevolucion
     */
    public Date getDataVenda() {
        return dataVenda;
    }

    /**
     * @param dataDevolucion the dataDevolucion to set
     */
    public void setDataDevolucionVenda(Date dataDevolucionVenda) {
        this.dataDevolucionVenda = dataDevolucionVenda;
    }  
    
   public String getImporteFormatoMoeda(){
       return Tools.getCurrency(this.importeVenda);
       
   }     
}
