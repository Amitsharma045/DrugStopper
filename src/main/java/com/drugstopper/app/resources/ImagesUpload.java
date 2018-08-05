package com.drugstopper.app.resources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.rest.RestResource;
import com.drugstopper.app.util.Constants;
import com.drugstopper.app.util.ImageUtil;

@Configuration
@Controller
@RequestMapping(value = "/drugstopper/admin")
@PropertySource("file:"+Constants.IMAGE_PROPERTY_LOC)
public class ImagesUpload extends RestResource{
	
	private  String UPLOADED_FOLDER = Constants.STATIC_IMAGE_LOC;
	
	private Class clazz = ImagesUpload.class;
	
	private JsonResponse jsonResponse;

	@RequestMapping(value = "/uploadImage", produces={"application/json"},
			method = RequestMethod.PUT)
	@ResponseBody
	public HashMap<String,Object> singleFileUpload(@RequestParam("file") MultipartFile file,
												   @RequestParam("desc") String[] desc) throws Exception {
			jsonResponse = new JsonResponse();
			if (file.isEmpty()) {
				jsonResponse.setStatusCode(ConstantProperty.INVALID_FILE);
				jsonResponse.setMessage(ConstantProperty.FILE_NOT_EXIST);
				log(clazz, "Please select a file to upload", ConstantProperty.LOG_ERROR);
				return sendResponse(jsonResponse);
			}

			// Get the file and save it somewhere
			String fileName = UPLOADED_FOLDER + file.getOriginalFilename().replaceAll(" ", "");
			byte[] bytes = file.getBytes();
			Path path = Paths.get(fileName);
			if (Files.notExists(Paths.get(fileName))) {
				Files.write(path, bytes);
				for(String description : desc) {
					String [] split = description.split(":");
					String lang="_"+split[0];
					String property=split[1];
					ImageUtil.writePropertiesFile(fileName.substring(fileName.lastIndexOf('/') + 1)+lang, property);
				}
				jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
				jsonResponse.setMessage("Successfully uploaded the image");
				return sendResponse(jsonResponse);
			} else {
				jsonResponse.setStatusCode(ConstantProperty.FILE_OVERRIDE_ERROR); 
				jsonResponse.setMessage("Please change the file name");
				log(clazz, "Please change the file name", ConstantProperty.LOG_ERROR);
				return sendResponse(jsonResponse);
			}  
	}
}
