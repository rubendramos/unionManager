package controllers;

import org.apache.commons.lang.StringUtils;
import play.*;
import play.mvc.*;

@Check("comite")
@With(Secure.class)
public class Avisos extends CRUD {    
     @Before
     static void setDefaultOrder() {
	if(StringUtils.isBlank(request.params.get("orderBy"))) {
		request.params.put("order", "desc");
		request.params.put("orderBy", "dataRealizacionAviso,dataARealizarAviso");
	}
}
}