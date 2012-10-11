package controllers;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import controllers.CRUD.ObjectType.ObjectField;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import models.*;
import org.hibernate.Session;
import org.jfree.util.ObjectTable;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.binding.Binder;
import play.data.validation.MaxSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.Model;
import play.db.Model.Factory;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.utils.Java;

import utils.*;

public abstract class CRUD extends Controller {

    @Before
    public static void addType() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        renderArgs.put("type", type);
    }

    public static void index() {
        if (getControllerClass() == CRUD.class) {
            forbidden();
        }
        render("CRUD/index.html");
    }

    public static void list(int page, String where, String search, String from, String searchFields,
            String orderBy, String order) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());


        //Activamos os filtros
        ((Session) JPA.em().getDelegate()).enableFilter("listadistribucion");


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

        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, whereClausule);
        Long count = type.count(search, searchFields, whereClausule);
        Long totalCount = type.count(null, null, (String) request.args.get("where"));



        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
        }
    }

    public static void listForeign(int page, String where, String search, String from, String searchFields,
            String orderBy, String order, String cacheId) {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }

        //Desactivamos filtros para a lista de foreign
        ((Session) JPA.em().getDelegate()).disableFilter("listadistribucion");
        ((Session) JPA.em().getDelegate()).enableFilter("permiso");

        Logger.debug("cacheid en listForeign", params.get("cacheId"));

        Logger.debug("cacheid en listForeign", cacheId);

        List<Model> objects = type.findPage(page, search, searchFields,
                orderBy, order, (String) request.args.get("where"));
        Long count = type.count(search, searchFields,
                (String) request.args.get("where"));
        Long totalCount = type.count(null, null,
                (String) request.args.get("where"));
        try {
            render("CRUD/listForeign.html", type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/listForeign.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }

    public static void show(String id, String page, String where) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);

        if (where == null || "".equals(where)) {
            where = type.createWhereFilterClausule();
        }
        String search = params.get("search");

        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            render(type, object, page, where, search);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object, page, where, search);
        }
    }

    public static void pecharEstado(String id, int page, String search, String searchFields,
            String orderBy, String order) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        UnionSecureModel pai = (UnionSecureModel) type.findById(id);
        notFoundIfNull(pai);

        pai.pecharRecursivo();
        Auditoria.facerAuditoria(pai, Seguridade.usuario(), play.i18n.Messages.get("generic.accionPechar", ""), Seguridade.organismo());

        if (page < 1) {
            page = 1;
        }

        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, type.createWhereFilterClausule());
        Long count = type.count(search, searchFields, type.createWhereFilterClausule());
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            render(type.controllerName + "/list.html", objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }

    public static void abrirEstado(String id, int page, String search, String searchFields,
            String orderBy, String order) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        UnionSecureModel object = (UnionSecureModel) type.findById(id);
        notFoundIfNull(object);
        object.abrirRecursivo();
        Auditoria.facerAuditoria(object, Seguridade.usuario(), play.i18n.Messages.get("generic.accionAbrir", ""), Seguridade.organismo());


        if (page < 1) {
            page = 1;
        }

        List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, type.createWhereFilterClausule());
        Long count = type.count(search, searchFields, type.createWhereFilterClausule());
        Long totalCount = type.count(null, null, (String) request.args.get("where"));
        try {
            render(type.controllerName + "/list.html", objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }

    public static void showFromCache(int page, String where, String search, String from, String searchFields,
            String orderBy, String order, String cacheId) throws Exception {


        ObjectType type = null;
        Model object = null;
        ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(cacheId);

        if (fkc != null) {


            ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();

            object = fkv.getPai();
            type = fkv.getTipoPai();
            String pp = type.getListAction().toString();
            notFoundIfNull(object);
            //Agora as variables de cache borranse cada 3 minutos
            //Cache.delete(cacheId);
            if (fkc.getIdPai() == null) {
                params.remove("cacheId");
            }

            try {
                render("CRUD/show.html", type, object, page, where, search, from);
            } catch (TemplateNotFoundException e) {
                Logger.error(e, "Erro aopintar dende a chache");
            }

        } else {
            flash.error(play.i18n.Messages.get("crud.cache.error"));
            render("errors/cache.html");
        }
    }

    public static void createFromCache(int page, String where, String search, String from, String searchFields,
            String orderBy, String order, String cacheId) throws Exception {



        ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(cacheId);

        if (fkc != null) {

            ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();



            Model object = fkv.getPai();
            ObjectType type = fkv.getTipoPai();
            String pp = type.getListAction().toString();
            notFoundIfNull(object);

            //Agora as variables de cache borranse cada 3 minutos
            //Cache.delete(cacheId);
            if (fkc.getIdPai() == null) {
                params.remove("cacheId");
            }


            try {
                if (fkc.idPai != null) {
                    render("CRUD/blankForeign.html", type, object, page, where, search, from, cacheId);
                } else {
                    render("CRUD/blank.html", type, object, page, where, search, from, cacheId);
                }
            } catch (TemplateNotFoundException e) {
                Logger.error(e, "Erro aopintar dende a chache");
            }

        } else {

            flash.error(play.i18n.Messages.get("crud.cache.error"));
            render("errors/cache.html");
        }

    }

    @SuppressWarnings("deprecation")
    public static void attachment(String id, String field) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        Field[] f = object.getClass().getFields();
        Object att = object.getClass().getField(field).get(object);
        if (att instanceof Model.BinaryField) {
            Model.BinaryField attachment = (Model.BinaryField) att;
            if (attachment == null || !attachment.exists()) {
                notFound();
            }
            response.contentType = attachment.type();
            renderBinary(attachment.get(), attachment.length());
        }
        // DEPRECATED
        if (att instanceof play.db.jpa.FileAttachment) {
            play.db.jpa.FileAttachment attachment = (play.db.jpa.FileAttachment) att;
            if (attachment == null || !attachment.exists()) {
                notFound();
            }
            renderBinary(attachment.get(), attachment.filename);
        }
        notFound();
    }

    public static void save(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Organismo org = Seguridade.organismo();

        Model object = (Model) type.findById(id);
        notFoundIfNull(object);
        String where = params.get("where");
        String search = params.get("search");
        String page = params.get("page");
        String listFromForm = params.get("from");

        Binder.bindBean(params.getRootParamNode(), "object", object);


        if (params.get("_save") != null || params.get("_saveAndContinue") != null) {
            validation.valid(object);
            if (validation.hasErrors()) {
                renderArgs.put("error",
                        play.i18n.Messages.get("crud.hasErrors"));
                try {
                    render(request.controller.replace(".", "/") + "/show.html",
                            type, object, page, where, search, listFromForm);
                } catch (TemplateNotFoundException e) {
                    render("CRUD/show.html", type, object, page, where, search, listFromForm);
                }
            }

            saveUnion(object, org, play.i18n.Messages.get("generic.accionActualizar"));

            flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
            if (params.get("_save") != null) {
                redirect(request.controller + ".list", page, where, search, listFromForm);
            }
            redirect(request.controller + ".show", object._key(), page, where, search, listFromForm);
        } else if (params.get("_addForeignKey") != null || params.get("_newForeignKey") != null
                || params.get("_deleteForeignKey") != null) {


            String sufixoCampo = getSufixoCampo(params.get("_addForeignKey"), params.get("_deleteForeignKey"), params.get("_newForeignKey"));

            String campo = params.get("_campo" + sufixoCampo);
            String tipo = params.get("_tipo" + sufixoCampo);
            ForeignKeyValues fkv = new ForeignKeyValues(object);
            fkv.setCampo(campo);
            fkv.setNomeTipoFillo(tipo);
            fkv.setProcedencia(fkv.PROCEDENCIA_SHOW);
            fkv.setControladorPai(getControllerClass());
            fkv.setWhere(where);
            fkv.setPage(page);
            fkv.setSearch(search);
            fkv.setFrom(listFromForm);


            if (params.get("_addForeignKey") != null) {
                fkv.setTipoForeign(fkv.FOREIGN_EXISTE);
            }

            if (params.get("_newForeignKey") != null) {
                fkv.setTipoForeign(fkv.FOREIGN_NOVA);

            }

            if (params.get("_deleteForeignKey") != null) {
                fkv.setTipoForeign(fkv.FOREIGN_EXISTE);

                String[] toDelete = params.getAll(campo + ".id");
                String[] AllMultiple = params.getAll(campo.replace(".", "_") + "_Todos");


                if (toDelete.length == 1) {
                    renderArgs.put("error", play.i18n.Messages.get("crud.delete.EmptySelect", "aaa"));
                } else {
                    fkv.deleteValueForeign(AllMultiple);
                }

                render("CRUD/show.html", type, object, page, where, search, listFromForm);

            }

            ForeignKeyCache fkc = new ForeignKeyCache(fkv);

            Cache.set(fkc.getId(), fkc, "3mn");
            params.put("cacheId", fkc.getId());

            fkc.redirectFillo();
        }
    }

    private static void saveUnion(Model object, Organismo org, String accion) throws Exception {
        setDataModificacion(object);
        setDataActual(object);
        setUser(object);
        setUserApuntamento(object);
        setNotificacionDe(object);
        setOrganismo(object, org);
        setAvisoFirma(object);
        setNomeFicheiroAdxunto(object);
        setSecureEstado(object);
        setRollAutenticado(object);
        object._save();
        Auditoria.facerAuditoria(object, User.find("byUsuario", Seguridade.connected()).<User>first(), accion, org);
        setOrganismoPai(object, org);
        actualizaMensaxeIdioma(object);
        setApuntamentoPermanencia(object, org);
        creaConvocaAsemblea(object, org);
    }

    private static void creaConvocaAsemblea(Model object, Organismo org) {
        try {
            if (object.getClass().getName().equals("models.Asemblea")) {

                Field t = object.getClass().getDeclaredField("documentacionAsemblea");
                Field t2 = object.getClass().getDeclaredField("ordeDoDia");

                String type = "java.util.Set";
                String property = "documentacionAsemblea";
                String property2 = "ordeDoDia";

                Object value = new PropertyDescriptor(property, object.getClass()).getReadMethod().invoke(object);
                Object value2 = new PropertyDescriptor(property2, object.getClass()).getReadMethod().invoke(object);

                Class<? extends Set> theClass = Class.forName(type).asSubclass(Set.class);
                Class<? extends Set> theClass2 = Class.forName(type).asSubclass(Set.class);

                Set obj = theClass.cast(value);
                Set obj2 = theClass2.cast(value2);

                t2.set(object, obj2);

                Documento doc = ((Asemblea) object).creaConvocaAsemblea(org);
                if (doc != null) {
                    obj.add(doc);
                }

                t.set(object, obj);
                object._save();

            }
        } catch (Exception ex) {
            Logger.debug("Non implementa Asemblea");
        }
    }

    private static void setOrganismo(Model object, Organismo org) {
        Field t = null;
        try {
            if (object instanceof models.UnionSecureModel) {
                t = object.getClass().getSuperclass().getSuperclass().getDeclaredField("organismo");
            } else if (object instanceof models.UnionModel) {
                t = object.getClass().getSuperclass().getDeclaredField("organismo");
            }
            t.set(object, org);
        } catch (Exception ex) {
            Logger.debug("Non implementa UnionModel");
        }
    }

    private static void setOrganismoPai(Model object, Organismo org) {
        Set<Organismo> orgFillos;
        try {

            if (object instanceof models.Organismo) {
                Organismo newOrg = (Organismo) object;
                if (newOrg.id != org.id) {
                    if (org.organismosFillo != null) {
                        orgFillos = org.organismosFillo;
                    } else {
                        orgFillos = new HashSet<Organismo>();
                    }

                    orgFillos.add((Organismo) object);
                    org.organismosFillo = orgFillos;
                    org._save();
                }
            }
        } catch (Exception ex) {
            Logger.debug("Non implementa Organismo");
        }
    }

    private static void setSecureEstado(Model object) {
        try {

            if (object instanceof models.UnionSecureModel) {
                Field t = object.getClass().getSuperclass().getDeclaredField("estado");
                TipoEstado te = TipoEstado.findById(Long.parseLong("1"));
                t.set(object, te);
            }

        } catch (Exception ex) {
            Logger.debug("Non implementa UnionModel");
        }
    }

    private static void setRollAutenticado(Model object) {
        try {

            if (object instanceof models.User) {
                User u = (User) object;
                Permiso permisoAutenticado = Permiso.findById(Long.parseLong("11"));
                //Set<models.Permiso> permisos=new Set<models.Permiso>();
                Set<models.Permiso> permisos = u.getPermisos();
                if (!permisos.contains(permisoAutenticado)) {
                    permisos.add(permisoAutenticado);
                }
                Field t = object.getClass().getSuperclass().getDeclaredField("permisos");
                t.set(object, permisos);
            }

        } catch (Exception ex) {
            Logger.debug("Non implementa user");
        }
    }

    private static void setUser(Model object) {
        try {
            User user = User.find("byUsuario", Seguridade.connected()).<User>first();
            Field t = object.getClass().getDeclaredField("user");
            t.set(object, user);
        } catch (Exception ex) {
            Logger.debug("Non implementa User");
        }
    }

    private static void setUserApuntamento(Model object) {
        try {
            User user = User.find("byUsuario", Seguridade.connected()).<User>first();
            Field t = object.getClass().getDeclaredField("usuarioApuntamento");
            t.set(object, user);
        } catch (Exception ex) {
            Logger.debug("Non implementa apuntamento");
        }
    }

    private static void setApuntamentoPermanencia(Model object, Organismo org) {
        try {
            if (object instanceof models.Apuntamento) {
                User user = User.find("byUsuario", Seguridade.connected()).<User>first();
                boolean permisoPermanencia = user.tenPermiso("Permanencia");
                boolean permisoTesoureria = user.tenPermiso("Tesoureria");
                if (permisoPermanencia && !permisoTesoureria) {
                    FollaConta fc = FollaConta.getFollaContasPermanencia(org, user);
                    Set<Apuntamento> apuntamentos = fc.apuntamentos;
                    Apuntamento ap = (Apuntamento) object;
                    apuntamentos.add(ap);
                    fc._save();
                }
            }

        } catch (Exception ex) {
            Logger.debug("Non implementa apuntamento");
        }
    }

    private static void deleteApuntamentoPermanencia(Model object, Organismo org) {
        try {
            if (object instanceof models.Apuntamento) {
                User user = User.find("byUsuario", Seguridade.connected()).<User>first();
                boolean permisoPermanencia = user.tenPermiso("Permanencia");
                boolean permisoTesoureria = user.tenPermiso("Tesoureria");
                if (permisoPermanencia && !permisoTesoureria) {
                    FollaConta fc = FollaConta.getFollaContasPermanencia(org, user);
                    Set<Apuntamento> apuntamentos = fc.apuntamentos;
                    Apuntamento ap = (Apuntamento) object;
                    apuntamentos.remove(ap);
                    fc._save();
                }
            }

        } catch (Exception ex) {
            Logger.debug("Non implementa apuntamento");
        }
    }

    private static void setNotificacionDe(Model object) {
        try {
            User user = User.find("byUsuario", Seguridade.connected()).<User>first();
            Field t = object.getClass().getDeclaredField("avisode");
            t.set(object, user);
        } catch (Exception ex) {
            Logger.debug("Non implementa NotificacionInterna");
        }
    }

    private static void setAvisoFirma(Model object) {
        try {
            User user = User.find("byUsuario", Seguridade.connected()).<User>first();
            Field t = object.getClass().getDeclaredField("firma");
            t.set(object, user);
        } catch (Exception ex) {
            Logger.debug("Non implementa Aviso");
        }
    }

    private static void setNomeFicheiroAdxunto(Model object) {
        try {

            Field t = object.getClass().getDeclaredField("nomeFicheiroAdxunto");
            Documento doc = (Documento) object;
            // doc.ficheiro.set(new FileInputStream(photo), MimeTypes.getContentType(photo.getName()));

            String nomeFicheiroAduxunto = doc.ficheiro.getFile().getName();
            t.set(object, nomeFicheiroAduxunto);
        } catch (Exception ex) {
            Logger.debug("Non implementa documento");
        }
    }

    private static void setDataActual(Model object) {
        try {


            Field d = object.getClass().getDeclaredField("dataAlta");
            Date data = (Date) d.get(object);
            if (data == null || "".equals(data.toString())) {
                d.set(object, Tools.getCurrentDate());
            }
        } catch (Exception ex) {
            Logger.debug("Non implementa dataActual");
        }
    }

    private static void setDataModificacion(Model object) {
        try {
            Field d = object.getClass().getDeclaredField("dataModificacion");
            d.set(object, Tools.getCurrentDate());
        } catch (Exception ex) {
            Logger.debug("Non implementa dataActual");
        }
    }

    private static void actualizaMensaxeIdioma(Model object) {
        try {

            MensaxesIdioma menIdi = (MensaxesIdioma) Class.forName("models.MensaxesIdioma").cast(object);
            LoadPropertiesFiles lpf = new LoadPropertiesFiles();
            lpf.loadMenasxeIdiomaProperties(menIdi);
        } catch (Exception ex) {
            Logger.debug("Non implementa MensaxesIdioma");
        }
    }

    private static void setPassEUser(Model object) {
        try {

            User user = (User) Class.forName("models.User").cast(object);
            String email = user.afiliado.persoa.getEmail();
            Field p = object.getClass().getDeclaredField("password");
            Field u = object.getClass().getDeclaredField("usuario");

            p.set(object, Tools.generatePassword());
            u.set(object, email);
        } catch (Exception ex) {
            Logger.debug("Non implementa User");
        }
    }

    private static String getSufixoCampo(String a, String b, String c) {
        String sufixoCampo = "";
        if (a != null) {
            sufixoCampo = a.trim();

        } else if (b != null) {
            sufixoCampo = b.trim();
        } else if (c != null) {
            sufixoCampo = c.trim();
        }

        return "_" + sufixoCampo.substring(sufixoCampo.lastIndexOf(" ") + 1, sufixoCampo.length()).trim();

    }

    public static void backForeignKey(String id, String cacheId) throws Exception {


        ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(cacheId);

        if (fkc != null) {
            ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();
            fkv.setControladorFillo(getControllerClass());


            String where = fkv.getWhere();
            String search = fkv.getSearch();
            String page = fkv.getPage();
            String listFromForm = fkv.getFrom();


            params.put("where", where);
            params.put("search", search);
            params.put("from", listFromForm);
            params.put("page", page);

            fkv.addValueForeign(id);


            fkc.redirectPai();


        } else {
            flash.error(play.i18n.Messages.get("crud.cache.error"));
            render("errors/cache.html");
        }

    }

    public static void blank(int page, String where, String search, String from) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);


        String whereClausule = null;




        if (from != null && "true".equals(from)) {

            type.setValuesFromSearch(where);

        } else {
            where = type.createWhereFilterClausule();
        }

        //String where=type.createWhereFilterClausule();

        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        try {
            render(type, object, page, where, search);
        } catch (TemplateNotFoundException e) {
            render("CRUD/blank.html", type, object, page, where, search);
        }
    }

    public static void blankForeign(int page, String where, String search, String from, String searchFields,
            String orderBy, String order, String cacheId) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);

        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();

        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/blankForeign.html", type, object, page, where, search, from);
        }
    }

    public static void blankForeignError(int page, String where, String search, String from, String searchFields,
            String orderBy, String order, String cacheId, String redirect) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);

        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = null;
        ForeignKeyCache fkc = null;

        if (cacheId != null) {
            if (redirect.equals("fillo")) {
                fkc = (ForeignKeyCache) Cache.get(params.get("cacheId"));
            } else if (redirect.equals("pai")) {
                fkc = (ForeignKeyCache) Cache.get(((ForeignKeyCache) Cache.get(params.get("cacheId"))).idPai);
            }
            ForeignKeyValues fkv = fkc.getForeignKeyValues();
            object = fkv.filloModel;

        } else {
            object = (Model) constructor.newInstance();
        }


        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
        }


        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/blankForeign.html", type, object, page, where, search, from);
        }
    }

    public static void create() throws Exception {
        boolean avisoEnviado = true;
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        ForeignKeyCache fkcPai = null;
        ForeignKeyCache fkcf = null;
        ForeignKeyValues fkv = null;
        ForeignKeyCache fkc = null;

        fkcf = (ForeignKeyCache) Cache.get(params.get("cacheId"));
        if (fkcf != null) {

            fkcPai = (ForeignKeyCache) Cache.get(fkcf.idPai);
        }

        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        Organismo org = Seguridade.organismo();
        User u = User.find("byUsuario", Seguridade.connected()).<User>first();

        String where = params.get("where");
        String search = params.get("search");
        String page = params.get("page");
        String from = params.get("from");

        Binder.bindBean(params.getRootParamNode(), "object", object);


        setPassEUser(object);
        if (params.get("_addForeignKey") != null || params.get("_newForeignKey") != null
                || params.get("_deleteForeignKey") != null) {



            String sufixoCampo = getSufixoCampo(params.get("_addForeignKey"), params.get("_deleteForeignKey"), params.get("_newForeignKey"));


            String campo = params.get("_campo" + sufixoCampo);
            String tipo = params.get("_tipo" + sufixoCampo);

            fkv = new ForeignKeyValues(object);
            fkv.setCampo(campo);
            fkv.setNomeTipoFillo(tipo);
            fkv.setProcedencia(fkv.PROCEDENCIA_BLANK);
            fkv.setControladorPai(getControllerClass());



            if (params.get("_addForeignKey") != null) {
                fkv.setTipoForeign(fkv.FOREIGN_EXISTE);
            }
            if (params.get("_newForeignKey") != null) {
                fkv.setTipoForeign(fkv.FOREIGN_NOVA);

            }
            if (params.get("_deleteForeignKey") != null) {
                fkv.setTipoForeign(fkv.FOREIGN_EXISTE);

                String[] toDelete = params.getAll(campo + ".id");
                String[] AllMultiple = params.getAll(campo.replace(".", "_") + "_Todos");


                if (toDelete.length == 1) {
                    renderArgs.put("error", play.i18n.Messages.get("crud.delete.EmptySelect", "aaa"));
                } else {
                    fkv.deleteValueForeign(AllMultiple);
                }

                render("CRUD/blank.html", type, object, page, where, search, from);

            }


            fkc = new ForeignKeyCache(fkv);

            params.put("cacheId", fkc.getId());
            if (fkcf != null) {
                ForeignKeyValues fkvf = fkcf.getForeignKeyValues();
                fkvf.setControladorFillo(getControllerClass());
                if (fkv.getNomeTipoFillo().equals(fkvf.getNomeTipoFillo())) {
                    fkc.setIdePai(fkcf.getIdPai());
                } else {
                    fkc.setIdePai(fkcf.getId());
                }
                Cache.set(fkcf.getId(), fkcf, "33mn");
            }
            Cache.set(fkc.getId(), fkc, "33mn");

            fkc.redirectFillo();

        } else if (params.get("_backForeign") != null) {
            if (fkcf.getIdPai() != null) {
                ForeignKeyValues fkvf = fkcf.getForeignKeyValues();
                if (fkvf.getControladorFillo() == null) {
                    fkvf.setControladorFillo(getControllerClass());
                }

                if (fkvf.getControladorPai().getName().equals(getControllerClass().getName())) {
                    params.put("cacheId", fkcPai.id);
                    fkcPai.redirectPai();
                } else {
                    params.put("cacheId", fkcf.id);
                    fkcf.redirectPai();
                }




            } else {
                //params.remove("cacheId");
                fkcf.redirectPai();

            }

        } else if (fkcf != null) {
            // ForeignKeyValues fkv = (ForeignKeyValues) fkcf.getForeignKeyValues();
            // type = ObjectType.get(fkv.controladorFillo);
            // constructor = type.entityClass.getDeclaredConstructor();
            //  constructor.setAccessible(true);
            //  object = (Model) constructor.newInstance();
            //  org = Seguridade.organismo();

            // constructor.setAccessible(true);
            //  
            //   Binder.bindBean(params.getRootParamNode(), "object", object);

            setPassEUser(object);

            validation.valid(object);
            if (validation.hasErrors()) {
                renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
                try {
                    // render(request.controller.replace(".", "/") + "/blankForeignError.html",type, object);
                    // fkcf.redirectFillo();
                    //render(type, object);

                    ForeignKeyValues fkvf = fkcf.getForeignKeyValues();
                    if (fkcPai != null) {
                        ForeignKeyValues fkvp = fkcPai.getForeignKeyValues();

                        if (fkvf.getControladorPai().getName().equals(getControllerClass().getName())) {
                            fkvp.setTipoForeign(fkvp.FOREIGN_VALIDATION_ERROR);
                            fkvp.setFilloModel(object);
                            params.put("redirect", "pai");
                            fkcPai.redirectFillo();

                        } else {
                            fkvf.setTipoForeign(fkvf.FOREIGN_VALIDATION_ERROR);
                            fkvf.setFilloModel(object);
                            params.put("redirect", "fillo");
                            fkcf.redirectFillo();
                        }
                    } else {
                        fkvf.setTipoForeign(fkvf.FOREIGN_VALIDATION_ERROR);
                        fkvf.setFilloModel(object);
                        params.put("redirect", "fillo");
                        fkcf.redirectFillo();

                    }


                    //render("CRUD/blankForeign.html", type, page, where, search, from, fkc.id);
                    //render("CRUD/blankForeign.html", type, object, page, where, search, from);
                } catch (TemplateNotFoundException e) {
                    render("CRUD/blankForeign.html", type, object);
                }
            }


            saveUnion(object, org, play.i18n.Messages.get("generic.accionNovoAlta"));

            flash.success(play.i18n.Messages.get("crud.created", type.modelName));

            if (fkcf.getIdPai() != null) {
                ForeignKeyValues fkvf = fkcf.getForeignKeyValues();
                ForeignKeyValues fkvp = fkcPai.getForeignKeyValues();



                if (fkvf.getControladorFillo() == null) {
                    fkvf.setControladorFillo(getControllerClass());
                }

                if (fkvf.getControladorPai().getName().equals(getControllerClass().getName())) {
                    fkvp.addValueForeign(object._key().toString());
                    params.put("cacheId", fkcPai.id);
                    fkcPai.redirectPai();
                } else {
                    fkvf.addValueForeign(object._key().toString());
                    params.put("cacheId", fkcf.id);
                    fkcf.redirectPai();
                }




            } else {
                //params.remove("cacheId");
                ForeignKeyValues fkvf = fkcf.getForeignKeyValues();
                if (fkvf.getControladorFillo() == null) {
                    fkvf.setControladorFillo(getControllerClass());
                }
                fkvf.addValueForeign(object._key().toString());
                fkcf.redirectPai();

            }



        } else {

            validation.valid(object);
            if (validation.hasErrors()) {
                renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
                try {
                    render(request.controller.replace(".", "/") + "/blank.html",
                            type, object, page, where, search, from);
                } catch (TemplateNotFoundException e) {
                    render("CRUD/blank.html", type, object, page, where, search, from);
                }
            }

            saveUnion(object, org, play.i18n.Messages.get("generic.accionNovoAlta"));


            //Si o obxeto do modelo implementa a interface notificable enviarase un 
            //aviso de sistema asociado a un tipo de aviso específico neste caso o 1.
            if (Notificable.class.isInstance(object)) {
                Notificable ob = (Notificable) object;
                NotificacionInterna ni = ob.getNotificacionInterna();
                ni.setAvisode(u);
                //ni.organismo = u.organismo;
                List users = User.usersNotificacions(u.organismo.id);
                HashSet<User> usersComite = new HashSet<User>(users);

                ni.setContactos(usersComite);
                ni._save();
            }

            if (User.class.isInstance(object)) {
                User uAlta = (User) object;
                uAlta.enviarNotificacionInternaAlta();
                if (!uAlta.mandarEmailAltaUsuario(u)) {
                    avisoEnviado = false;
                }
            }

            if (Afiliado.class.isInstance(object)) {
                Afiliado aAlta = (Afiliado) object;
                if (!aAlta.mandarEmailAltaAfiliado(aAlta, u)) {
                    avisoEnviado = false;
                }

            }

            if (avisoEnviado) {
                flash.success(play.i18n.Messages.get("crud.created", type.modelName));
            } else {
                flash.success(play.i18n.Messages.get("crud.createdNonAvisado", type.modelName));
            }


            if (params.get("_save") != null) {
                redirect(request.controller + ".list", page, where, search, from);
            }
            if (params.get("_saveAndAddAnother") != null) {
                redirect(request.controller + ".blank", page, where, search, from);
            }


            redirect(request.controller + ".show", object._key(), page, where, search, from);

        }


    }

    public static void delete(String id) throws Exception {
        boolean avisoEnviado = true;
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        String where = type.createWhereFilterClausule();
        Model object = type.findById(id);
        String search = params.get("search");
        String page = params.get("page");
        User u = User.find("byUsuario", Seguridade.connected()).<User>first();

        notFoundIfNull(object);
        Organismo org = Seguridade.organismo();
        try {
            if (User.class.isInstance(object)) {
                User uAlta = (User) object;
                if (!uAlta.mandarEmailBaixaUsuario(u)) {
                    avisoEnviado = false;
                }

            }

            if (Afiliado.class.isInstance(object)) {
                Afiliado aBaixa = (Afiliado) object;

                if (!aBaixa.mandarEmailBaixaAfiliado(aBaixa, u)) {
                    avisoEnviado = false;
                }
            }


            Auditoria.facerAuditoria(object, u, "Borrar", org);
            deleteApuntamentoPermanencia(object, org);
            object._delete();

        } catch (Exception e) {

            if (e instanceof javax.persistence.PersistenceException) {
                flash.error(play.i18n.Messages.get("crud.delete.constraintViolationError", type.modelName));
            } else {
                flash.error(play.i18n.Messages.get("crud.delete.error", type.modelName));
            }
            redirect(request.controller + ".list", page, where, search, "true");

        }

        if (!avisoEnviado) {
            flash.success(play.i18n.Messages.get("crud.deletedNonAvisado", type.modelName));
        } else {
            flash.success(play.i18n.Messages.get("crud.deleted", type.modelName));
        }

        redirect(request.controller + ".list", page, where, search, "true");
    }

    protected static ObjectType createObjectType(
            Class<? extends Model> entityClass) {
        return new ObjectType(entityClass);
    }

    // ~~~~~~~~~~~~~
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface For {

        Class<? extends Model> value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Exclude {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Hidden {
    }

    // ~~~~~~~~~~~~~
    static int getPageSize() {
        return Integer.parseInt(Play.configuration.getProperty("crud.pageSize",
                "30"));
    }

    public static class ObjectType implements Comparable<ObjectType> {

        public Class<? extends Controller> controllerClass;
        public Class<? extends Model> entityClass;
        public String name;
        public String modelName;
        public String controllerName;
        public String keyName;
        public Factory factory;

        public ObjectType(Class<? extends Model> modelClass) {
            this.modelName = modelClass.getSimpleName();
            Logger.debug(this.modelName);
            this.entityClass = modelClass;
            this.factory = Model.Manager.factoryFor(entityClass);
            this.keyName = factory.keyName();
        }

        @SuppressWarnings("unchecked")
        public ObjectType(String modelClass) throws ClassNotFoundException {
            this((Class<? extends Model>) Play.classloader.loadClass(modelClass));
        }

        public static ObjectType forClass(String modelClass)
                throws ClassNotFoundException {
            return new ObjectType(modelClass);
        }

        public static ObjectType get(Class<? extends Controller> controllerClass) {
            Class<? extends Model> entityClass = getEntityClassForController(controllerClass);
            if (entityClass == null
                    || !Model.class.isAssignableFrom(entityClass)) {
                return null;
            }
            ObjectType type;
            try {
                type = (ObjectType) Java.invokeStaticOrParent(controllerClass,
                        "createObjectType", entityClass);
            } catch (Exception e) {
                Logger.error(e,
                        "Couldn't create an ObjectType. Use default one.");
                type = new ObjectType(entityClass);
            }
            type.name = controllerClass.getSimpleName().replace("$", "");
            type.controllerName = controllerClass.getSimpleName().toLowerCase().replace("$", "");
            type.controllerClass = controllerClass;
            return type;
        }

        @SuppressWarnings("unchecked")
        public static Class<? extends Model> getEntityClassForController(
                Class<? extends Controller> controllerClass) {
            if (controllerClass.isAnnotationPresent(For.class)) {
                return controllerClass.getAnnotation(For.class).value();
            }
            for (Type it : controllerClass.getGenericInterfaces()) {
                if (it instanceof ParameterizedType) {
                    ParameterizedType type = (ParameterizedType) it;
                    if (((Class<?>) type.getRawType()).getSimpleName().equals(
                            "CRUDWrapper")) {
                        return (Class<? extends Model>) type.getActualTypeArguments()[0];
                    }
                }
            }
            String name = controllerClass.getSimpleName().replace("$", "");
            name = "models." + name.substring(0, name.length() - 1);
            try {
                return (Class<? extends Model>) Play.classloader.loadClass(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        public Object getListAction() {
            return Router.reverse(controllerClass.getName().replace("$", "")
                    + ".list");
        }

        public Object getBlankAction() {
            return Router.reverse(controllerClass.getName().replace("$", "")
                    + ".blank");
        }

        private Field getOrganismoField() throws NoSuchFieldException {
            Field t = null;
            try {
                t = this.entityClass.getDeclaredField("organismo");
                return t;
            } catch (NoSuchFieldException exa) {
                try {
                    //Filtramos sempre polo id do organismo
                    t = this.entityClass.getSuperclass().getDeclaredField("organismo");
                    return t;
                } catch (NoSuchFieldException exb) {
                    try {
                        t = this.entityClass.getSuperclass().getSuperclass().getDeclaredField("organismo");
                        return t;
                    } catch (NoSuchFieldException exc) {
                        throw new NoSuchFieldException();
                    }
                }
            }
        }

        public Long count(String search, String searchFields, String where) {

            try {

                //Filtramos sempre polo id do organismo
                Field t = getOrganismoField();
                if (Seguridade.organismo() != null && !"".equals(Seguridade.organismo().id)) {
                    if (where == null || "".equals(where)) {
                        where = " organismo_id='" + Seguridade.organismo().id + "'";
                    } else {
                        where = where + " and  organismo_id='" + Seguridade.organismo().id + "'";
                    }
                }
            } catch (NoSuchFieldException ex) {
                Logger.debug("Non implementa UnionModel");
            }

            return factory.count(searchFields == null ? new ArrayList<String>()
                    : Arrays.asList(searchFields.split("[ ]")), search, where);
        }

        @SuppressWarnings("unchecked")
        public List<Model> findPage(int page, String search,
                String searchFields, String orderBy, String order, String where) {
            int offset = (page - 1) * getPageSize();
            try {

                //Filtramos sempre polo id do organismo

                if (Seguridade.organismo() != null && !"".equals(Seguridade.organismo().id)) {
                    Field t = getOrganismoField();
                    if (where == null || "".equals(where)) {
                        where = " organismo_id='" + Seguridade.organismo().id + "'";
                    } else {
                        where = where + " and  organismo_id='" + Seguridade.organismo().id + "'";
                    }
                }

            } catch (NoSuchFieldException ex) {
                Logger.debug("Non implementa UnionModel");
            }


            List<String> properties = searchFields == null ? new ArrayList<String>(
                    0) : Arrays.asList(searchFields.split("[ ]"));
            return Model.Manager.factoryFor(entityClass).fetch(offset,
                    getPageSize(), orderBy, order, properties, search, where);
        }

        public Model findById(String id) throws Exception {
            if (id == null) {
                return null;
            }

            Factory factory = Model.Manager.factoryFor(entityClass);
            Object boundId = Binder.directBind(id, factory.keyType());
            return factory.findById(boundId);
        }

        private String recoverValuesFromString(ObjectField f, String where, String dataIniFin) {
            String valor = "";
            String clave = f.property.name;
            int ini = 0;
            int fin = 0;
            if (where.indexOf(clave) != -1) {
                if (f.property.isRelation) {

                    ini = where.indexOf("='", where.indexOf(clave)) + 2;
                    fin = where.indexOf("'", where.indexOf("='", where.indexOf(clave)) + 2);

                    if (fin == -1) {
                        fin = where.length();
                    }
                    valor = where.substring(ini, fin);

                } else if ("date".equals(f.type)) {
                    if (where.indexOf("between convert(") != -1) {
                        if ("ini".equals(dataIniFin)) {
                            ini = where.indexOf("between convert('", where.indexOf(clave)) + 17;
                            fin = where.indexOf("'", ini);
                        }

                        if ("fin".equals(dataIniFin)) {
                            ini = where.indexOf("and convert('", where.indexOf(clave)) + 13;
                            fin = where.indexOf("'", ini);
                        }
                    } else {
                        if ("ini".equals(dataIniFin)) {
                            ini = where.indexOf(">=convert('", where.indexOf(clave)) + 11;
                            fin = where.indexOf("'", ini);
                        }

                        if ("fin".equals(dataIniFin)) {
                            ini = where.indexOf("<=convert('", where.indexOf(clave)) + 11;
                            fin = where.indexOf("'", ini);
                        }

                    }
                    valor = where.substring(ini, fin);
                    valor = Tools.getLocaleDateFormat(Tools.getDataBaseDateFormat(valor));

                } else {
                    ini = where.indexOf("like '%", where.indexOf(clave)) + 7;
                    fin = where.indexOf("%'", where.indexOf(clave)) + 7;
                    valor = where.substring(ini, fin);

                }

            }

            return valor;
        }

        public void setValuesFromSearch(String where) {
            List<ObjectField> fields = this.getFields();

            if (where != null) {
                for (ObjectField f : fields) {
                    if (f.filtro) {

                        if (f.property.isRelation) {
                            params.put("object." + f.name + ".id", recoverValuesFromString(f, where, null));

                        } else if ("date".equals(f.type)) {

                            params.put("object." + f.name + ".ini", recoverValuesFromString(f, where, "ini"));
                            params.put("object." + f.name + ".fin", recoverValuesFromString(f, where, "fin"));

                        } else {
                            params.put("object." + f.name, recoverValuesFromString(f, where, null));

                        }
                    }
                }
            }
        }

        public String createWhereFilterClausule() {
            String where = "";
            List<ObjectField> fields = this.getFields();

            for (ObjectField f : fields) {
                if (f.filtro) {
                    String valor = "";
                    if (f.property.isRelation) {
                        valor = (String) params.get("object." + f.name + ".id");
                    } else if ("date".equals(f.type)) {

                        String ini = (String) params.get("object." + f.name + ".ini");
                        String fin = (String) params.get("object." + f.name + ".fin");

                        if (ini != null && !"".equals(ini)) {
                            if (fin != null && !"".equals(fin)) {
                                valor = "between convert('" + Tools.dateToDateDataBaseFormat(ini) + "',date) and convert('" + Tools.dateToDateDataBaseFormat(fin) + "',date)";
                            } else {
                                valor = ">=convert('" + Tools.dateToDateDataBaseFormat(ini) + "',date)";
                            }
                        } else if (fin != null && !"".equals(fin)) {
                            valor = "<=convert('" + Tools.dateToDateDataBaseFormat(fin) + "',date)";
                        }


                    } else {
                        valor = params.get("object." + f.name);

                    }


                    if (valor != null && !"".contains(valor)) {
                        if ("date".equals(f.type)) {
                            valor = f.property.name + " " + (String) valor;
                        } else if ("relation".equals(f.type)) {
                            valor = f.property.name + " ='" + (String) valor + "'";
                        } else {
                            valor = f.property.name + " like '%" + (String) valor + "%'";
                        }
                        if (!"".equals(where)) {
                            where += " and ";
                        }
                        where += valor;
                    }
                }
            }


            if ("".equals(where)) {
                where = null;
            }

            return where;

        }

        public List<ObjectField> getFields() {
            List<ObjectField> fields = new ArrayList<ObjectField>();
            List<ObjectField> hiddenFields = new ArrayList<ObjectField>();
            for (Model.Property f : factory.listProperties()) {
                ObjectField of = new ObjectField(f, this.modelName);
                if (of.type != null) {
                    if (of.type.equals("hidden")) {
                        User user = User.find("byUsuario", Seguridade.connected()).<User>first();
                        if (of.name.equals("organismo") && user.tenPermiso("Admin")) {
                            fields.add(of);
                        } else {
                            hiddenFields.add(of);
                        }
                    } else {
                        fields.add(of);
                    }
                }
            }

            hiddenFields.addAll(fields);
            return hiddenFields;
        }

        public ObjectField getField(String name) {
            for (ObjectField field : getFields()) {
                if (field.name.equals(name)) {
                    return field;
                }
            }
            return null;
        }

        @Override
        public int compareTo(ObjectType other) {
            return modelName.compareTo(other.modelName);
        }

        @Override
        public String toString() {
            return modelName;
        }

        public static class ObjectField {

            private Model.Property property;
            public String type = "unknown";
            public String name;
            public boolean multiple;
            public boolean required;
            public boolean addForeignKey;            
            public boolean newForeignKey;
            public String pai = "unknown";
            public String tipo = "unknown";
            public String nomeCampo = "unknown";
            public boolean filtro;

            @SuppressWarnings("deprecation")
            public ObjectField(Model.Property property, String pai) {
                Field field = property.field;
                User user = User.find("byUsuario", Seguridade.connected()).<User>first();

                this.property = property;
                this.pai = pai;
                if (CharSequence.class.isAssignableFrom(field.getType())) {
                    type = "text";
                    if (field.isAnnotationPresent(MaxSize.class)) {
                        int maxSize = field.getAnnotation(MaxSize.class).value();
                        if (maxSize > 100) {
                            type = "longtext";
                        }
                    }
                    if (field.isAnnotationPresent(Password.class)) {
                        type = "password";
                    }
                    if (field.isAnnotationPresent(PlayHora.class)) {
                        type = "hora";
                    }                    
                }
                if (Number.class.isAssignableFrom(field.getType())
                        || field.getType().equals(double.class)
                        || field.getType().equals(int.class)
                        || field.getType().equals(long.class)) {
                    
                if (field.isAnnotationPresent(PlayCurrency.class)) {
                    type = "playCurrency";
                } else{
                    type = "number";
                }                     
                    
                
                }
                if (Boolean.class.isAssignableFrom(field.getType())
                        || field.getType().equals(boolean.class)) {
                    type = "boolean";
                }
                if (Date.class.isAssignableFrom(field.getType())) {
                    type = "date";

                }
                if (property.isRelation) {
                    type = "relation";
                }
                if (property.isMultiple) {
                    multiple = true;
                }
                if (Model.BinaryField.class.isAssignableFrom(field.getType())
                        || /**
                         * DEPRECATED *
                         */
                        play.db.jpa.FileAttachment.class.isAssignableFrom(field.getType())) {
                    type = "binary";
                }
                if (field.getType().isEnum()) {
                    type = "enum";
                }
                if (property.isGenerated) {
                    type = null;
                }
                if (field.isAnnotationPresent(Required.class)) {
                    required = true;
                }
                if (field.isAnnotationPresent(Hidden.class)) {
                    type = "hidden";
                }
                if (field.isAnnotationPresent(Exclude.class)) {
                    type = null;
                }
                if (field.isAnnotationPresent(AddForeignKey.class)) {
                    addForeignKey = true;
                }
                
              
                if (field.isAnnotationPresent(NewForeignKey.class)) {
                    newForeignKey = true;
                }
                if (field.isAnnotationPresent(AddFiltro.class)) {
                    filtro = true;
                }
                if (java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
                    type = null;
                }
                name = field.getName();

                if (multiple) {
                    tipo = field.toGenericString();
                    tipo = tipo.substring(tipo.indexOf("<"), tipo.indexOf(">"));
                    tipo = tipo.substring(tipo.indexOf(".") + 1);
                } else {
                    tipo = field.getType().getSimpleName();
                }

                nomeCampo = field.toGenericString();
                nomeCampo = nomeCampo.substring(nomeCampo.lastIndexOf(".") + 1, nomeCampo.length());
            }

            public List<Object> getChoices() {
                return property.choices.list();
            }
        }
    }

    private static class ForeignKeyCache implements Serializable {

        private String id;
        private ForeignKeyValues foreignKeyValues;
        private String idPai;

        public ForeignKeyCache(ForeignKeyValues foreignKeyValues) {
            this.id = generateCache();
            this.foreignKeyValues = foreignKeyValues;
        }

        public String getId() {
            return this.id;
        }

        public void setIdePai(String idPai) {
            this.idPai = idPai;
        }

        public String getIdPai() {
            return this.idPai;
        }

        public ForeignKeyValues getForeignKeyValues() {
            return this.foreignKeyValues;
        }

        public void redirectFillo() {

            String where = params.get("where");
            String search = params.get("search");
            String page = params.get("page");
            String from = params.get("from");
            String cacheId = params.get("cacheId");
            String redirect = params.get("redirect");

            this.getForeignKeyValues().setWhere(where);
            this.getForeignKeyValues().setPage(page);
            this.getForeignKeyValues().setSearch(search);
            this.getForeignKeyValues().setFrom(from);


            if (this.getForeignKeyValues().getTipoForeign().equals(this.getForeignKeyValues().FOREIGN_NOVA)) {
                redirect(this.getForeignKeyValues().getFillo().getSimpleName() + "s" + ".blankForeign", page, where, search, from, null, null, null, cacheId);

            } else if (this.getForeignKeyValues().getTipoForeign().equals(this.getForeignKeyValues().FOREIGN_VALIDATION_ERROR)) {
                redirect(this.getForeignKeyValues().getFillo().getSimpleName() + "s" + ".blankForeignError", page, where, search, from, null, null, null, cacheId, redirect);

            } else {
                redirect(this.getForeignKeyValues().getFillo().getSimpleName() + "s" + ".listForeign", page, where, search, from, null, null, null, cacheId);
            }
        }

        public void redirectPai() {

            String where = this.getForeignKeyValues().getWhere();
            String search = this.getForeignKeyValues().getSearch();
            String page = this.getForeignKeyValues().getPage();
            String from = this.getForeignKeyValues().getFrom();
            String cacheId = params.get("cacheId");
            params.put("where", where);
            params.put("search", search);
            params.put("from", from);
            params.put("page", page);

            if (this.getForeignKeyValues().getProcedencia().equals(this.getForeignKeyValues().PROCEDENCIA_SHOW)) {
                redirect(this.getForeignKeyValues().getControladorPai().getSimpleName() + ".showFromcache", page, where, search, from, null, null, null, cacheId);
            } else {
                redirect(this.getForeignKeyValues().getControladorPai().getSimpleName() + ".createFromCache", page, where, search, from, null, null, null, cacheId);
            }

        }

        private String generateCache() {
            Random random = new Random();
            String cache = Long.toString(random.nextLong());
            return cache;
        }

        public String toString() {
            return "id: " + this.id
                    + " ForeigKeyValues: " + this.foreignKeyValues.toString();
        }
    }

    private static class ForeignKeyValues implements Serializable {

        public final String PROCEDENCIA_BLANK = "blank";
        public final String PROCEDENCIA_SHOW = "show";
        public final String FOREIGN_NOVA = "nova";
        public final String FOREIGN_EXISTE = "existe";
        public final String FOREIGN_VALIDATION_ERROR = "validationError";
        private Class<? extends Model> fillo;
        private String nomeTipoFillo;
        private ObjectType tipoFillo;
        private Class<? extends Controller> controladorFillo;
        private Model pai;
        private Model filloModel;
        private String nomeTipoPai;
        private ObjectType tipoPai;
        private Class<? extends Controller> controladorPai;
        private String campo;
        private String procedencia;
        private String tipoForeign;
        private String where;
        private String page;
        private String search;
        private String from;

        public ForeignKeyValues(Model pai) {
            this.pai = pai;
            this.campo = campo;

        }

        public String toString() {
            return "pai: " + this.getPai()
                    + "  tipoPai: " + this.getTipoPai()
                    + "  ControladorPai: " + this.getControladorPai().getName()
                    + "  nomeTipoFillo: " + this.getNomeTipoFillo()
                    + "  Fillo: " + this.getFillo()
                    + "  campo: " + this.getCampo();

        }

        public void addValueForeign(String idFillo) {

            Object filloCache;

            try {

                ObjectType typeFillo = this.getTipoFillo();
                Model a = typeFillo.findById(idFillo);
                filloCache = Class.forName(this.getNomeTipoFillo()).cast(a);


                Field t = this.getPai().getClass().getDeclaredField(this.getCampo());


                if ("Set".equals(t.getType().getSimpleName())) {
                    String type = "java.util.Set";
                    String property = this.getCampo();
                    Object value = new PropertyDescriptor(property, this.getPai().getClass()).getReadMethod().invoke(this.getPai());

                    Class<? extends Set> theClass = Class.forName(type).asSubclass(Set.class);
                    Set obj = theClass.cast(value);
                    obj.add(filloCache);

                    t.set(this.getPai(), obj);

                } else {

                    t.set(this.getPai(), filloCache);
                }



            } catch (IllegalAccessException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (NoSuchFieldException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (SecurityException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (ClassNotFoundException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (Exception ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            }
        }

        private Collection removeEmptyValues(String[] values) {

            List<String> list = Arrays.asList(values);
            List<String> result = new ArrayList<String>();

            for (String str : list) {
                if (str != null && !str.isEmpty()) {
                    result.add(str);
                }
            }

            return result;

        }

        public void deleteValueForeign(String[] idAllValues) {

            Object filloCache;
            String type = "java.util.Set";
            String property = this.getCampo();
            Collection todos = Arrays.asList(idAllValues);

            try {



                Class<? extends Set> theClass = Class.forName(type).asSubclass(Set.class);
                //Class<? extends Model> modelClass = Class.forName(this.getNomeTipoFillo()).asSubclass(Model.class);
                Object value = new PropertyDescriptor(property, this.getPai().getClass()).getReadMethod().invoke(this.getPai());
                Set obj = theClass.cast(value);

                ObjectType typeFillo = new ObjectType(this.getNomeTipoFillo());
                Field t = this.getPai().getClass().getDeclaredField(this.getCampo());


                for (Object id : todos) {
                    Model a = typeFillo.findById(id.toString());
                    filloCache = Class.forName(this.getNomeTipoFillo()).cast(a);
                    if (obj.contains(filloCache)) {
                        obj.remove(filloCache);
                    } else {
                        obj.add(filloCache);
                    }

                }


                t.set(this.getPai(), obj);


            } catch (IllegalAccessException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (NoSuchFieldException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (SecurityException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (ClassNotFoundException ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            } catch (Exception ex) {
                Logger.error(ex, "Erro ao pintar a paxina");
            }
        }

        /**
         * @return the fillo
         */
        public Class<? extends Model> getFillo() {
            return fillo;
        }

        /**
         * @param fillo the fillo to set
         */
        public void setFillo(String NomeTipoFillo) {

            Class<? extends Model> res = null;

            try {
                res = (Class<? extends Model>) Class.forName(NomeTipoFillo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            this.fillo = res;

        }

        /**
         * @return the nomeTipoFillo
         */
        public String getNomeTipoFillo() {
            return nomeTipoFillo;
        }

        /**
         * @param nomeTipoFillo the nomeTipoFillo to set
         */
        public void setNomeTipoFillo(String nomeTipoFillo) {
            this.nomeTipoFillo = "models." + nomeTipoFillo;
            setFillo(this.nomeTipoFillo);

        }

        /**
         * @return the tipoFillo
         */
        public ObjectType getTipoFillo() {
            return tipoFillo;
        }

        /**
         * @param tipoFillo the tipoFillo to set
         */
        public void setTipoFillo(ObjectType tipoFillo) {
            this.tipoFillo = tipoFillo;
        }

        /**
         * @return the controladorFillo
         */
        public Class<? extends Controller> getControladorFillo() {
            return controladorFillo;
        }

        /**
         * @param controladorFillo the controladorFillo to set
         */
        public void setControladorFillo(Class<? extends Controller> controladorFillo) {
            this.controladorFillo = controladorFillo;
            this.setTipoFillo(ObjectType.get(controladorFillo));
        }

        /**
         * @return the pai
         */
        public Model getPai() {
            return pai;
        }

        /**
         * @param pai the pai to set
         */
        public void setPai(Model pai) {
            this.pai = pai;
        }

        /**
         * @return the nomeTipoPai
         */
        public String getNomeTipoPai() {
            return nomeTipoPai;
        }

        /**
         * @param nomeTipoPai the nomeTipoPai to set
         */
        public void setNomeTipoPai(String nomeTipoPai) {
            this.nomeTipoPai = nomeTipoPai;
        }

        /**
         * @return the tipoPai
         */
        public ObjectType getTipoPai() {
            return tipoPai;
        }

        /**
         * @param tipoPai the tipoPai to set
         */
        public void setTipoPai(ObjectType tipoPai) {
            this.tipoPai = tipoPai;
        }

        /**
         * @return the controladorPai
         */
        public Class<? extends Controller> getControladorPai() {
            return controladorPai;

        }

        /**
         * @param controladorPai the controladorPai to set
         */
        public void setControladorPai(Class<? extends Controller> controladorPai) {
            this.controladorPai = controladorPai;
            this.setTipoPai(ObjectType.get((Class<? extends Controller>) controladorPai));
        }

        /**
         * @return the campo
         */
        public String getCampo() {
            return campo;
        }

        /**
         * @param campo the campo to set
         */
        public void setCampo(String campo) {
            this.campo = campo.substring(campo.indexOf(".") + 1);
        }

        /**
         * @return the procedencia
         */
        public String getProcedencia() {
            return procedencia;
        }

        /**
         * @param procedencia the procedencia to set
         */
        public void setProcedencia(String procedencia) {
            this.procedencia = procedencia;
        }

        /**
         * @return the tipoForeign
         */
        public String getTipoForeign() {
            return tipoForeign;
        }

        /**
         * @param tipoForeign the tipoForeign to set
         */
        public void setTipoForeign(String tipoForeign) {
            this.tipoForeign = tipoForeign;
        }

        /**
         * @return the where
         */
        public String getWhere() {
            return where;
        }

        /**
         * @param where the where to set
         */
        public void setWhere(String where) {
            this.where = where;
        }

        /**
         * @return the page
         */
        public String getPage() {
            return page;
        }

        /**
         * @param page the page to set
         */
        public void setPage(String page) {
            this.page = page;
        }

        /**
         * @return the search
         */
        public String getSearch() {
            return search;
        }

        /**
         * @param search the search to set
         */
        public void setSearch(String search) {
            this.search = search;
        }

        /**
         * @return the from
         */
        public String getFrom() {
            return from;
        }

        /**
         * @param from the from to set
         */
        public void setFrom(String from) {
            this.from = from;
        }

        /**
         * @return the filloModel
         */
        public Model getFilloModel() {
            return filloModel;
        }

        /**
         * @param filloModel the filloModel to set
         */
        public void setFilloModel(Model filloModel) {
            this.filloModel = filloModel;
        }
    }
}
