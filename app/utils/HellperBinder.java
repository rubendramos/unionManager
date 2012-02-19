package utils;
import play.Logger;
import play.data.binding.*;

import org.joda.time.format.*;

import java.lang.annotation.*;
import java.sql.Time;
import java.text.ParseException;
import java.util.regex.*;

@Global
public class HellperBinder implements TypeBinder<Class> {
   

    public Object bind(String name, Annotation[] annotations, String value, Class clazz, java.lang.reflect.Type genericType) {
        if (value == null || value.length() == 0) {
        	play.Logger.debug("Nulollllllll");
            return null;
       } 
        play.Logger.debug("Value: "+ clazz.getName());
            return  clazz;

        //throw new IllegalArgumentException("Invalid field");
    }
}