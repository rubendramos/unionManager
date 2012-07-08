package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import utils.AddForeignKey;
import utils.NewForeignKey;

import java.util.*;
import org.hibernate.annotations.Filter;
import play.data.validation.Email;
import utils.AddFiltro;
import utils.Tools;

@Entity
public class Contacto extends UnionModel {

    @ManyToOne
    @Required
    @AddFiltro
    public TipoContacto tipoContacto;
    
    @Required
    @MaxSize(50)
    public String nome;
    
    @Required
    @MaxSize(50)
    public String apelido1;
    
    @MaxSize(50)
    public String apelido2;
    
    @Email
    @Required
    @MaxSize(50)
    public String email;
    
    @ManyToOne
    @AddForeignKey
    public Enderezo enderezo;
    
    @MaxSize(9)
    public String telefono;
    
    @MaxSize(9)
    public String telefonoMobil;

    public Contacto(TipoContacto tipoContacto, String nome, String apelido1, String apelido2,
            String email, Enderezo enderezo, String telefono, String telefonoMobil) {

        this.tipoContacto = tipoContacto;
        this.apelido1 = apelido1;
        this.apelido2 = apelido2;
        this.email = email;
        this.enderezo = enderezo;
        this.telefono = telefono;
        this.telefonoMobil = telefonoMobil;
        this.nome=nome;
    }

    public String toString() {
        return this.tipoContacto + "  " + this.nome + " " + this.apelido1 + " " + this.apelido2;
    }

    public String getNomeCompletoContacto() {
        String res = null;
        if (this.nome != null && !"".equals(nome)) {
            if (this.apelido1 != null && !"".equals(apelido1)) {
                res = "\"" + this.nome + " " + this.apelido1 + "\"";
            }

        }
        return res;
    }

    public String formateaDireccionContacto() {
        String parseEmail = "";
        String parseNome = "";

        if (this.email != null && !"".endsWith(email)) {
            parseEmail = "<" + this.email + ">";

            if (this.nome != null && !"".equals(nome)) {
                if (this.apelido1 != null && !"".equals(apelido1)) {
                    parseNome = "\"" + this.nome + " " + this.apelido1 + "\"";
                }
            }

            return parseNome + parseEmail;

        }
        return null;
    }
}