package com.drugstopper.app.resources;
/**
 * @author rpsingh
 *
 */
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.rest.RestResource;

@Controller
@RequestMapping(value = "/drugstopper/api/common")
public class CommonResources extends RestResource {

	private JsonResponse jsonResponse;
	
	@RequestMapping(value = "/v1.0/anyUpdate", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> anyUpdate() throws Exception {
		jsonResponse=new JsonResponse();
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_PROCESSED);
		jsonResponse.setUpdateFlag("false");;
		return sendResponse(jsonResponse);
	}
}
