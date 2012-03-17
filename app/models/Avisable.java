/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Set;

/**
 *
 * @author ruben
 */
public interface Avisable {
    
    String getAsunto();
    String getContido();
    void sendAviso(String asunto);                 
}
