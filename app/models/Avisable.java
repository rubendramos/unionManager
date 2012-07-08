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
    


    Enderezo getLugarCelebracion();
    Set<Documento> getAdxuntos();
    String getHoraCelebracion();
    String getTitulo();
}
