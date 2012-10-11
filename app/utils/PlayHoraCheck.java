package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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


	public class PlayHoraCheck extends AbstractAnnotationCheck<PlayCurrency> {
	 
	

	    /**
	     *
	     * {@inheritDoc}
	     */
	    @Override
	    public boolean isSatisfied(Object validatedObject, Object value,
	            OValContext context, Validator validator) {
                
                
	    	return evaluaFormatoHora(value.toString());
	    }
            
            private boolean evaluaFormatoHora(String value){
                boolean res=false;
                
                int separadorHoras=value.indexOf(":");
                if(separadorHoras>0){
                    String hora=value.substring(0,separadorHoras);
                    String minutos=value.substring(separadorHoras+1,value.length());
                    
                    if(validaHora(hora) && validaMinutos(minutos)){
                        res=true;                    
                    }
                }
                
                return res;
            }
            
            private boolean validaHora(String hora){
               ArrayList<String> dixitosValidos=new ArrayList<String>(Arrays.asList("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"));
               boolean res=false;
               if(hora.length()!=2 ){
                        res=false;
               }else  if(dixitosValidos.contains(hora)){
                        res=true;
               }
               
               return res;
            }

            private boolean validaMinutos(String minutos){
               ArrayList<String> primeirDixitoValidos=new ArrayList<String>(Arrays.asList("0","1","2","3","4","5"));
               ArrayList<String> segundoDixitoValidos=new ArrayList<String>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));
               boolean res=false;
               if(minutos.length()!=2 ){
                        res=false;
               }else{
                   String primeiroDixito=minutos.substring(0,1);
                   String segundoDixito=minutos.substring(1,2);
                   
                   if(primeirDixitoValidos.contains(primeiroDixito)){
                        if(segundoDixitoValidos.contains(segundoDixito)){
                            res=true;
                        }else{
                            res=false;
                        }
                   }else{
                       res=false;
                   }
               }
               
               return res;
            }
	 
}
