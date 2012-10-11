/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Set;
import javax.persistence.Entity;



/**
 *
 * @author ruben
 */


public interface Avisable {
    

    String getAsunto();
    Set<ListaDistribucion>  getContactos();
    Set<Documento> getAdxuntos();
    String getContido();
}
