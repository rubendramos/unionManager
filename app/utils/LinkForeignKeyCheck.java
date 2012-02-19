package utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.GenericModel;
import play.db.jpa.JPQL;
import play.db.jpa.Model;
import play.exceptions.UnexpectedException;


	public class LinkForeignKeyCheck extends AbstractAnnotationCheck<LinkForeignKey> {
	  final static String mes = "utils.LinkForeignKey";
	  	  
	

	    /**
	     *
	     * {@inheritDoc}
	     */
	    @Override
	    public boolean isSatisfied(Object validatedObject, Object value,
	            OValContext context, Validator validator) {
	    	return true;
	    }

	 
}
