package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import utils.AddForeignKey;
import utils.NewForeignKey;
import utils.Tools;

@Entity
public class FollaConta extends UnionSecureModel {
    
    @Required
    @MaxSize(50)
    public String descricion;
    
    @Required
    @AddForeignKey
    @NewForeignKey
    @ManyToMany
    public Set<Apuntamento> apuntamentos;
    
    @Required
    public Date dataAlta;
    
    public Date dataBaixa;        
  
    public FollaConta(String descricion, Set<Apuntamento> apuntamentos,Date dataAlta,Date dataBaixa){
    	this.descricion=descricion;
        this.apuntamentos=apuntamentos;
        this.dataAlta=dataAlta;
        this.dataBaixa=dataBaixa;
    
    }
      
    public String toString() {
        return this.descricion;
    }

     public static FollaConta createFollaContasAfiliacion(Mes mes, Ano ano,Organismo organismo){
        String descricion=play.i18n.Messages.get("follaContas.follaContasMesAfiliados", mes.descricion,ano.descricion);               
        FollaConta fc=new FollaConta(descricion, null, Tools.getCurrentDate(), null);
        TipoEstado te=TipoEstado.findById(Long.parseLong("1"));
        HashSet<Apuntamento> ap=new HashSet<Apuntamento>();
        fc.apuntamentos=ap;
        fc.setOrganismo(organismo);
        fc.setEstado(te);
        fc._save();    
        return fc;
    }
     
    public static FollaConta getFollaContasAfiliacion(Organismo organismo,Mes mes, Ano ano){
        String descricion=play.i18n.Messages.get("follaContas.follaContasMesAfiliados", mes,ano);                       
        FollaConta fc=FollaConta.find("byDescricionAndOrganismo",descricion, organismo).first();
        if(fc==null){
           fc= createFollaContasAfiliacion(mes,ano,organismo);
        }
        return fc;
    }    
    
     public static FollaConta createFollaContasPermanencia(User u,Organismo organismo){
        String descricion=play.i18n.Messages.get("follaContas.follaContasPermanencia", u.afiliado.persoa.nomeCompleto);                       
        LibroConta lc=LibroConta.getLibroContasPermanencia(organismo);
        Set<FollaConta> follasconta=lc.follasContas;
        FollaConta fc=new FollaConta(descricion, null, Tools.getCurrentDate(), null);
        TipoEstado te=TipoEstado.findById(Long.parseLong("1"));
        HashSet<Apuntamento> ap=new HashSet<Apuntamento>();
        fc.apuntamentos=ap;
        fc.setOrganismo(organismo);
        fc.setEstado(te);
        fc._save();    
        follasconta.add(fc);
        lc._save();
        return fc;
    }
     
    public static FollaConta getFollaContasPermanencia(Organismo organismo,User u){
        String descricion=play.i18n.Messages.get("follaContas.follaContasPermanencia", u.afiliado.persoa.nomeCompleto);                       
        FollaConta fc=FollaConta.find("byDescricionAndOrganismo",descricion, organismo).first();
        if(fc==null){
           fc= createFollaContasPermanencia(u,organismo);
        }
        return fc;
    }    
    
}
