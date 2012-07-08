/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.InputStream;
import models.*;
import java.util.Set;
import javax.persistence.Entity;
import play.mvc.Scope.Params;



/**
 *
 * @author ruben
 */


public interface InformeI {
    


    InputStream xeneraInforme(Organismo org,Params params);
    String getNomeInforme();
    String getInforme();
    String getFormato();
}
