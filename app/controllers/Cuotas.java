package controllers;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import models.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import play.*;
import play.db.Model;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;

@Check("tesoureria")
@With(Secure.class)
public class Cuotas extends CRUD {

    private static void createListaCuotasPorMesEAno(Long idMes, Long idAno) {
        List<Afiliado> afiliadosAlta = Afiliado.getListaAfiliadosAlta();


        for (Afiliado af : afiliadosAlta) {
            Cuota c = new Cuota(af, (Ano) Ano.findById(idAno), (Mes) Mes.findById(idMes), false);
            c.setOrganismo(Seguridade.organismo());
            c._save();

        }
    }

    private static void actualizaLibroEFollaContasDeAfiliacion(Mes mes, Ano ano) {
        Organismo organismo = Seguridade.organismo();
        LibroConta libroContasAfiliacion = LibroConta.getLibroContasAfiliacion(organismo);
        Set follasContas = libroContasAfiliacion.follasContas;
        FollaConta follaConta = FollaConta.getFollaContasAfiliacion(organismo, mes, ano);
        follasContas.add(follaConta);
        follaConta._save();
        libroContasAfiliacion._save();
    }

    private static void engadeApuntamentoCuotaAFollaCuotas(Mes mes, Ano ano, Afiliado af, User u) {
        Organismo organismo = Seguridade.organismo();
        FollaConta follaConta = FollaConta.getFollaContasAfiliacion(organismo, mes, ano);
        Apuntamento ap = Apuntamento.createApuntamentoCuotaAfiliado(u, af, organismo, mes, ano);
        Set<Apuntamento> apuntamentos = follaConta.apuntamentos;
        apuntamentos.add(ap);
        follaConta._save();

    }

    private static void eliminaApuntamentoCuotaAFollaCutas(Mes mes, Ano ano, Afiliado af, User u) {
        Organismo organismo = Seguridade.organismo();
        FollaConta follaConta = FollaConta.getFollaContasAfiliacion(organismo, mes, ano);
        Apuntamento ap = Apuntamento.getApuntamentoCuotaAfiliado(u, af, organismo,mes,ano);
        Set<Apuntamento> apuntamentos = follaConta.apuntamentos;
        apuntamentos.remove(ap);
        follaConta._save();
        ap._delete();
    }

    public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());


        //Activamos os filtros
        String sano = params.get("object.ano.id");
        String smes = params.get("object.mes.id");




        String whereClausule = null;

        notFoundIfNull(type);

        if (page < 1) {
            page = 1;
        }

        if (from != null && "true".equals(from)) {
            if (where != null && !"".equals(where)) {
                whereClausule = where;
            }
            type.setValuesFromSearch(where);

        } else {
            whereClausule = type.createWhereFilterClausule();
        }

        if ((sano == null || "".equals(sano)) || (smes == null || "".equals(smes))) {

            Calendar cal = Calendar.getInstance();
            int iAno = cal.get(Calendar.YEAR);
            int iMes = cal.get(Calendar.MONTH)+1;
            
            Long longMes = Long.parseLong(Integer.toString(iMes));
            Long longAno = Long.parseLong(Integer.toString(iAno));

            String stringAno = Integer.toString(iAno);
            String StringMes = Integer.toString(iMes);
            Ano anoInicial = (Ano) Ano.find("byDescricion", stringAno).first();
            Mes mesInicial = Mes.findById(longMes);
            Cuota cuota=Cuota.find("byMesAndAno", mesInicial,anoInicial).first();
            
            
            
            if(cuota==null){
                createListaCuotasPorMesEAno(mesInicial.id, anoInicial.id);
                actualizaLibroEFollaContasDeAfiliacion(mesInicial, anoInicial);
            }
            
            
            if (whereClausule == null || "".equals(whereClausule)) {
                whereClausule = " ano='" + anoInicial.id + "' and mes='" + mesInicial.id + "'";
                type.setValuesFromSearch(whereClausule);
            }

        }



        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);
        if (sano != null && !"".equals(sano) && smes != null && !"".equals(smes)) {
            if (objects.isEmpty()) {
                Ano anoInicial = (Ano) Ano.findById(Long.parseLong(sano));
                Mes mesInicial = Mes.findById(Long.parseLong(smes));
                createListaCuotasPorMesEAno(Long.parseLong(smes), Long.parseLong(sano));
                actualizaLibroEFollaContasDeAfiliacion(mesInicial, anoInicial);
                objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);

            }
        }

        Long count = type.count(search, searchFields, whereClausule);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));



        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }

    public static String updatePago(String id, String isPagado, String afiliadoId) {

        Cuota cuota = Cuota.findById(Long.parseLong(id));
        Mes mes = cuota.mes;
        Ano ano = cuota.ano;
        Afiliado af = Afiliado.findById(Long.parseLong(afiliadoId));
        User u = User.find("byUsuario", Seguridade.connected()).<User>first();
        cuota.isPagado = Boolean.parseBoolean(isPagado);
        cuota._save();
        if (cuota.isPagado) {
            engadeApuntamentoCuotaAFollaCuotas(mes, ano, af, u);
        } else {
            eliminaApuntamentoCuotaAFollaCutas(mes, ano, af, u);
        }
        flash.success(play.i18n.Messages.get("crud.saved", "Cuota"));

        return cuota.getEstadoPagamento();

    }
}