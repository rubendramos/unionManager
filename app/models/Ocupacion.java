package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import utils.AddFiltro;

@Entity
public class Ocupacion extends Model {

    @Required
    @MaxSize(50)
    public String descricion;
    @Required
    @ManyToOne
    @AddFiltro
    public Ramo ramo;

    public Ocupacion(String descricion, Ramo ramo) {
        this.descricion = descricion;
        this.ramo = ramo;
    }

    public String toString() {
        String res = "";
        if (this.descricion.length() <= 75) {
            res = this.descricion;
        } else {
            res = this.descricion.substring(0, 75) + "...";
        }
        return res;

    }
}