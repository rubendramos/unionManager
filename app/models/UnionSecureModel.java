package models;

import controllers.CRUD;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import javax.persistence.*;
import play.data.validation.Required;


import play.db.jpa.*;

@MappedSuperclass
public class UnionSecureModel extends UnionModel {

    @ManyToOne
    @CRUD.Hidden
    public TipoEstado estado;

    /**
     * @return the estado
     */
    public TipoEstado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(TipoEstado estado) {
        this.estado = estado;
    }

    private void pecharEstado() {
        TipoEstado te = TipoEstado.findById(Long.parseLong("2"));
        this.setEstado(te);
        this._save();
    }

    private void abrirEstado() {
        TipoEstado te = TipoEstado.findById(Long.parseLong("1"));
        this.setEstado(te);
        this._save();
    }

    public void pecharRecursivo() throws Exception {

        this.pecharEstado();
        
        Field[] fillos = this.getClass().getFields();

        ArrayList<Field> aFields = new ArrayList<Field>(Arrays.asList(fillos));

        for (Field field : aFields) {

            if (!field.getName().equals("id") && !field.getName().equals("willBeSaved")) {

                if ("Set".equals(field.getType().getSimpleName())) {


                    Object value = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod().invoke(this);
                    Class<? extends Set> theClass = Class.forName("java.util.Set").asSubclass(Set.class);
                    Set objSet = theClass.cast(value);

                    for (Object obj : objSet) {
                        if (obj instanceof models.UnionSecureModel) {
                            UnionSecureModel u = (UnionSecureModel) obj;
                            u.pecharEstado();
                            u.pecharRecursivo();
                        }

                    }

                } else {

                    Object obj = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod().invoke(this);

                    if (obj instanceof models.UnionSecureModel) {
                        UnionSecureModel u = (UnionSecureModel) obj;
                        u.pecharEstado();
                        u.pecharRecursivo();
                    }

                }
            }
        }

    }
    
  public void abrirRecursivo() throws Exception {

        this.abrirEstado();
        
        Field[] fillos = this.getClass().getFields();

        ArrayList<Field> aFields = new ArrayList<Field>(Arrays.asList(fillos));

        for (Field field : aFields) {

            if (!field.getName().equals("id") && !field.getName().equals("willBeSaved")) {

                if ("Set".equals(field.getType().getSimpleName())) {


                    Object value = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod().invoke(this);
                    Class<? extends Set> theClass = Class.forName("java.util.Set").asSubclass(Set.class);
                    Set objSet = theClass.cast(value);

                    for (Object obj : objSet) {
                        if (obj instanceof models.UnionSecureModel) {
                            UnionSecureModel u = (UnionSecureModel) obj;
                            u.abrirEstado();
                            u.abrirRecursivo();
                        }

                    }

                } else {

                    Object obj = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod().invoke(this);

                    if (obj instanceof models.UnionSecureModel) {
                        UnionSecureModel u = (UnionSecureModel) obj;
                        u.abrirEstado();
                        u.abrirRecursivo();
                    }

                }
            }
        }

    }    
}
