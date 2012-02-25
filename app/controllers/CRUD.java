package controllers;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.*;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.binding.Binder;
import play.data.validation.MaxSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.db.Model;
import play.db.Model.Factory;
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

    public static void list(int page, String search, String searchFields,
            String orderBy, String order) {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }
        List<Model> objects = type.findPage(page, search, searchFields,
                orderBy, order, (String) request.args.get("where"));
        Long count = type.count(search, searchFields,
                (String) request.args.get("where"));
        Long totalCount = type.count(null, null,
                (String) request.args.get("where"));
        try {
            render(type, objects, count, totalCount, page, orderBy, order);
        } catch (TemplateNotFoundException e) {
            render("CRUD/list.html", type, objects, count, totalCount, page,
                    orderBy, order);
        }
    }

    public static void listForeign(int page, String search, String searchFields,
            String orderBy, String order, String cacheId) {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        if (page < 1) {
            page = 1;
        }

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

    public static void show(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object);
        }
    }

    public static void showFromCache(String cacheId) throws Exception {



        ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(cacheId);
        ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();



        Model object = fkv.getPai();
        ObjectType type = fkv.getTipoPai();
        String pp = type.getListAction().toString();
        notFoundIfNull(object);

        //borramos a cache
        Cache.delete(cacheId);

        try {
            render("CRUD/show.html", type, object);
        } catch (TemplateNotFoundException e) {
            Logger.error(e, "Erro aopintar dende a chache");
        }
    }

    public static void createFromCache(String cacheId) throws Exception {



        ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(cacheId);
        ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();



        Model object = fkv.getPai();
        ObjectType type = fkv.getTipoPai();
        String pp = type.getListAction().toString();
        notFoundIfNull(object);

        //borramos a cache
        //  Cache.delete(cacheId);

        try {
            render("CRUD/blank.html", type, object);
        } catch (TemplateNotFoundException e) {
            Logger.error(e, "Erro aopintar dende a chache");
        }
    }

    @SuppressWarnings("deprecation")
    public static void attachment(String id, String field) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
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
        Model object = type.findById(id);
        notFoundIfNull(object);
        Binder.bindBean(params.getRootParamNode(), "object", object);        

        if (params.get("_save") != null || params.get("_saveAndContinue") != null) {
            validation.valid(object);
            if (validation.hasErrors()) {
                renderArgs.put("error",
                        play.i18n.Messages.get("crud.hasErrors"));
                try {
                    render(request.controller.replace(".", "/") + "/show.html",
                            type, object);
                } catch (TemplateNotFoundException e) {
                    render("CRUD/show.html", type, object);
                }
            }
            object._save();
            flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
            if (params.get("_save") != null) {
                redirect(request.controller + ".list");
            }
            redirect(request.controller + ".show", object._key());
        } else if (params.get("_addForeignKey") != null || params.get("_newForeignKey") != null
                || params.get("_deleteForeignKey") != null) {

            String campo = params.get("_campo");
            String tipo=params.get("_tipo");
            ForeignKeyValues fkv = new ForeignKeyValues(object);
            fkv.setCampo(campo);
            fkv.setNomeTipoFillo(tipo);
            fkv.setProcedencia(fkv.PROCEDENCIA_SHOW);
            fkv.setControladorPai(getControllerClass());


            if (params.get("_addForeignKey") != null) {                
                fkv.setTipoForeign(fkv.FOREIGN_EXISTE);
            }

            if (params.get("_newForeignKey") != null) {            
                fkv.setTipoForeign(fkv.FOREIGN_NOVA);

            }

            if (params.get("_deleteForeignKey") != null) {   
                fkv.setTipoForeign(fkv.FOREIGN_EXISTE);
                
                String[] toDelete = params.getAll("object.afiliados.id");
                String[] AllMultiple = params.getAll("object_afiliados_Todos");

                
                if (toDelete.length == 1) {
                    renderArgs.put("error", play.i18n.Messages.get("crud.delete.EmptySelect","aaa"));                    
                } else {
                    fkv.deleteValueForeign(AllMultiple);
                }

                render("CRUD/show.html", type, object);

            }

            ForeignKeyCache fkc = new ForeignKeyCache(fkv);

            Cache.set(fkc.getId(), fkc);
            params.put("cacheId", fkc.getId());

            redirect(fkc.redirectFillo());
        }
    }

    public static void backForeignKey(String id, String cacheId) throws Exception {


        ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(cacheId);
        ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();
        fkv.setControladorFillo(getControllerClass());
        fkv.addValueForeign(id);

        redirect(fkc.redirectPai());
    }

    public static void blank() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/blank.html", type, object);
        }
    }

    public static void blankForeign() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        try {
            render(type, object);
        } catch (TemplateNotFoundException e) {
            render("CRUD/blankForeign.html", type, object);
        }
    }

    public static void create() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);


        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        Binder.bindBean(params.getRootParamNode(), "object", object);

        if (params.get("_addForeignKey") != null || params.get("_newForeignKey") != null) {

            String campo = params.get("_campo");
            String tipo = params.get("_tipo");

            ForeignKeyValues fkv = new ForeignKeyValues(object);
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


            ForeignKeyCache fkc = new ForeignKeyCache(fkv);

            Cache.set(fkc.getId(), fkc);
            params.put("cacheId", fkc.getId());

            redirect(fkc.redirectFillo());

        }

        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/blank.html",
                        type, object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/blank.html", type, object);
            }
        }
        object._save();
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        if (params.get("_save") != null) {
            redirect(request.controller + ".list");
        }
        if (params.get("_saveAndAddAnother") != null) {
            redirect(request.controller + ".blank");
        }


        redirect(request.controller + ".show", object._key());
    }

    public static void createForeign() throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Model object = (Model) constructor.newInstance();
        Binder.bindBean(params.getRootParamNode(), "object", object);
        validation.valid(object);
        if (validation.hasErrors()) {
            renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
            try {
                render(request.controller.replace(".", "/") + "/blankForeign.html",
                        type, object);
            } catch (TemplateNotFoundException e) {
                render("CRUD/blankForeign.html", type, object);
            }
        }
        object._save();
        flash.success(play.i18n.Messages.get("crud.created", type.modelName));
        if (params.get("_saveAndBackForeign") != null) {

            ForeignKeyCache fkc = (ForeignKeyCache) Cache.get(params.get("cacheId"));
            ForeignKeyValues fkv = (ForeignKeyValues) fkc.getForeignKeyValues();
            ObjectType.get(getControllerClass());
            fkv.setControladorFillo(getControllerClass());

            fkv.addValueForeign(object._key().toString());

            redirect(fkc.redirectPai());

        }
        redirect(request.controller + ".show", object._key());
    }

    public static void delete(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        Model object = type.findById(id);
        notFoundIfNull(object);
        try {
            object._delete();
        } catch (Exception e) {
            flash.error(play.i18n.Messages.get("crud.delete.error",
                    type.modelName));
            redirect(request.controller + ".show", object._key());
        }
        flash.success(play.i18n.Messages.get("crud.deleted", type.modelName));
        redirect(request.controller + ".list");
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

        public Long count(String search, String searchFields, String where) {

            return factory.count(searchFields == null ? new ArrayList<String>()
                    : Arrays.asList(searchFields.split("[ ]")), search, where);
        }

        @SuppressWarnings("unchecked")
        public List<Model> findPage(int page, String search,
                String searchFields, String orderBy, String order, String where) {
            int offset = (page - 1) * getPageSize();
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

        public List<ObjectField> getFields() {
            List<ObjectField> fields = new ArrayList<ObjectField>();
            List<ObjectField> hiddenFields = new ArrayList<ObjectField>();
            for (Model.Property f : factory.listProperties()) {
                ObjectField of = new ObjectField(f, this.modelName);
                if (of.type != null) {
                    if (of.type.equals("hidden")) {
                        hiddenFields.add(of);
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
            public boolean linkToForeignKey;
            public String pai = "unknown";
            public String tipo = "unknown";
            public String sinatura = "unknown";

            @SuppressWarnings("deprecation")
            public ObjectField(Model.Property property, String pai) {
                Field field = property.field;

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
                }
                if (Number.class.isAssignableFrom(field.getType())
                        || field.getType().equals(double.class)
                        || field.getType().equals(int.class)
                        || field.getType().equals(long.class)) {
                    type = "number";
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
                if (field.isAnnotationPresent(LinkForeignKey.class)) {
                    linkToForeignKey = true;
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

                sinatura = field.toString();
            }

            public List<Object> getChoices() {
                return property.choices.list();
            }
        }
    }

    private static class ForeignKeyCache implements Serializable {

        private String id;
        private ForeignKeyValues foreignKeyValues;

        public ForeignKeyCache(ForeignKeyValues foreignKeyValues) {
            this.id = generateCache();
            this.foreignKeyValues = foreignKeyValues;
        }

        public String getId() {
            return this.id;
        }

        public ForeignKeyValues getForeignKeyValues() {
            return this.foreignKeyValues;
        }

        public String redirectFillo() {

            if (this.getForeignKeyValues().getTipoForeign().equals(this.getForeignKeyValues().FOREIGN_NOVA)) {
                return "/" + this.getForeignKeyValues().getFillo().getSimpleName() + "s/blankForeign?cacheId=" + this.getId();
            } else {
                return "/" + this.getForeignKeyValues().getFillo().getSimpleName() + "s/listForeign?cacheId=" + this.getId();
            }
        }

        public String redirectPai() {
            if (this.getForeignKeyValues().getProcedencia().equals(this.getForeignKeyValues().PROCEDENCIA_SHOW)) {
                return "/" + this.getForeignKeyValues().getPai().getClass().getSimpleName() + "s/showFromcache?cacheId=" + this.getId();
            } else {
                return "/" + this.getForeignKeyValues().getPai().getClass().getSimpleName() + "s/createFromCache?cacheId=" + this.getId();
            }

        }

        private String generateCache() {
            Random random = new Random();
            String cache = Long.toString(random.nextLong());
            return cache;
        }

        public String toString() {
            return "id: " + this.id
                    + " ForeigKeyValues: " + this.foreignKeyValues.toString()
                    + " RedirecFillo: " + this.redirectFillo()
                    + " RedirecPai: " + this.redirectPai();
        }
    }

    private static class ForeignKeyValues implements Serializable {

        public final String PROCEDENCIA_BLANK = "blank";
        public final String PROCEDENCIA_SHOW = "show";
        public final String FOREIGN_NOVA = "nova";
        public final String FOREIGN_EXISTE = "existe";
        private Class<? extends Model> fillo;
        private String nomeTipoFillo;
        private ObjectType tipoFillo;
        private Class<? extends Controller> controladorFillo;
        private Model pai;
        private String nomeTipoPai;
        private ObjectType tipoPai;
        private Class<? extends Controller> controladorPai;
        private String campo;
        private String procedencia;
        private String tipoForeign;

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
    }
}
