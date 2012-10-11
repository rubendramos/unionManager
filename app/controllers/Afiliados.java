package controllers;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.*;
import notificacions.Notificador;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.hibernate.Filter;
import org.hibernate.Session;
import play.*;
import play.db.Model;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.*;
import play.utils.Java;
import utils.Tools;

//@Check("comite")
@Check("Afiliados")
@With(Secure.class)
public class Afiliados extends CRUD {

    @Before
    public static void setFilters() {

        User user = User.find("byUsuario", Seguridade.connected()).first();

        if (!user.tenPermiso("Admin")) {
            Filter filter = ((Session) JPA.em().getDelegate()).enableFilter("organismo");
            filter.setParameter("organismo_id", user.organismo.id);

        }
    }        
        
   
    public static void setDefaultOrder() {
	if(StringUtils.isBlank(request.params.get("orderBy"))) {
		request.params.put("order", "ASC");
		request.params.put("orderBy", "persoa.nomeCompleto");
	}        
    }


//    public static void notificaAlta(Afiliado afiliado) {
//
//
//        Aviso av = new Aviso();
//
//
//        try {
//            if (Notificador.notificacionXenerica(av)) {
//                flash.success("Please check your emails ...");
//                flash.put("email", av);
//
//            }
//        } catch (Exception e) {
//            Logger.error(e, "Mail error");
//        }
//        flash.error("Oops (the email cannot be sent)...");
//        flash.put("email", av);
//    }
    private static void darDeBaixaAfiliado(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        User user = User.find("byUsuario", Seguridade.connected()).<User>first();
        Afiliado afiliado = (Afiliado) type.findById(id);
        afiliado.setDataBaixa(Tools.getCurrentDate());
        afiliado.setEstadoAfiliado((TipoEstadoAfiliado) TipoEstadoAfiliado.findById(Long.parseLong("2")));   
        afiliado.mandarEmailBaixaAfiliado(afiliado,user);
        afiliado._save();
    }

    private static void darDeAltaAfiliado(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        User user = User.find("byUsuario", Seguridade.connected()).<User>first();
        Afiliado afiliado = (Afiliado) type.findById(id);
        afiliado.setDataBaixa(null);
        afiliado.setEstadoAfiliado((TipoEstadoAfiliado) TipoEstadoAfiliado.findById(Long.parseLong("1")));
        afiliado._save();
        afiliado.mandarEmailAltaAfiliado(afiliado,user);
    }

    public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) {
        String whereClausule = null;
        ObjectType type = ObjectType.get(getControllerClass());
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

        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);
        Long count = type.count(search, searchFields, whereClausule);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("afiliados/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }

    public static void darDebaixaERepintar(String id, String page, String search) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        String where = type.createWhereFilterClausule();
        darDeBaixaAfiliado(id);
        list(Integer.parseInt(page), where, search, "false", null, null, null);

    }

    public static void darDeAltaERepintar(String id, String page, String search) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        String where = type.createWhereFilterClausule();
        darDeAltaAfiliado(id);
        list(Integer.parseInt(page), where, search, "false", null, null, null);

    }

    public static void xeneraFichaAfiliado(String idAfiliado, String page, String search) {

        Map reportParams = new HashMap();

        if (idAfiliado == null || "".equals(idAfiliado)) {
            idAfiliado = "-1";
        }

        String nomeInforme = "fichaAfiliado";

        reportParams.put("AFILIADO_ID", idAfiliado);
        reportParams.put("TITULO", play.i18n.Messages.get("informe.fichaAfiliado.titulo"));
        reportParams.put("SDNI", play.i18n.Messages.get("informe.fichaAfiliado.dni"));
        reportParams.put("SNOME", play.i18n.Messages.get("informe.fichaAfiliado.nome"));
        reportParams.put("SAPELIDO1", play.i18n.Messages.get("informe.fichaAfiliado.apelido1"));
        reportParams.put("SAPELIDO2", play.i18n.Messages.get("informe.fichaAfiliado.apelido2"));
        reportParams.put("SDATANACEMENTO", play.i18n.Messages.get("informe.fichaAfiliado.dataNacemento"));
        reportParams.put("STELEFONO", play.i18n.Messages.get("informe.fichaAfiliado.telefono"));
        reportParams.put("STELEFONOMOBIL", play.i18n.Messages.get("informe.fichaAfiliado.telefonoMobil"));
        reportParams.put("SEMAIL", play.i18n.Messages.get("informe.fichaAfiliado.eMail"));
        reportParams.put("SRAMO", play.i18n.Messages.get("informe.fichaAfiliado.ramo"));
        reportParams.put("SESTADO", play.i18n.Messages.get("informe.fichaAfiliado.estado"));
        reportParams.put("SOCUPACION", play.i18n.Messages.get("informe.fichaAfiliado.ocupacion"));
        reportParams.put("SMILITA", play.i18n.Messages.get("informe.fichaAfiliado.milita"));
        reportParams.put("SDATAALTA", play.i18n.Messages.get("informe.fichaAfiliado.dataalta"));
        reportParams.put("SDATABAIXA", play.i18n.Messages.get("informe.fichaAfiliado.databaixa"));
        reportParams.put("SENDEREZO", play.i18n.Messages.get("informe.generic.enderezo"));
        reportParams.put("SLOCALIDADE", play.i18n.Messages.get("informe.generic.localidade"));
        reportParams.put("SCODIGOPOSTAL", play.i18n.Messages.get("informe.generic.codigoPostal"));
        reportParams.put("SCONCELLO", play.i18n.Messages.get("informe.generic.concello"));
        reportParams.put("SPROVINCIA", play.i18n.Messages.get("informe.generic.provincia"));
        reportParams.put("SCOMUNIDADE", play.i18n.Messages.get("informe.generic.comunidade"));
        reportParams.put("SSEXO", play.i18n.Messages.get("informe.fichaAfiliado.sexo"));

        reportParams.put("SDATOSENDEREZO", play.i18n.Messages.get("informe.fichaAfiliado.datosEnderezo"));
        reportParams.put("SDATOSSINDICAIS", play.i18n.Messages.get("informe.fichaAfiliado.datosSindicais"));
        reportParams.put("SDATOSPERSOAIS", play.i18n.Messages.get("informe.fichaAfiliado.datosPersoais"));
        reportParams.put("SESTADOAFILIACION", play.i18n.Messages.get("informe.fichaAfiliado.estadoAfiliacion"));
        reportParams.put("SNOMESINDICATO", play.i18n.Messages.get("informe.fichaAfiliado.sindicato"));

        reportParams.put("SSI", play.i18n.Messages.get("generic.si"));
        reportParams.put("SNO", play.i18n.Messages.get("generic.no"));

        reportParams.put("STELEFONO", play.i18n.Messages.get("informe.generic.telefono"));
        reportParams.put("SFAX", play.i18n.Messages.get("informe.generic.fax"));
        reportParams.put("SACORREOS", play.i18n.Messages.get("informe.generic.apartadoCorreos"));
        reportParams.put("SEMAIL", play.i18n.Messages.get("informe.generic.email"));


        renderBinary(utils.BaseJasperReports.download_pdf(nomeInforme, reportParams), nomeInforme + ".pdf");

    }
}