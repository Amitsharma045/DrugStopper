package com.drugstopper.app.resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.drugstopper.app.bean.Image;
import com.drugstopper.app.json.JsonResponse;
import com.drugstopper.app.property.ConstantProperty;
import com.drugstopper.app.rest.RestResource;
import com.drugstopper.app.util.Constants;
import com.drugstopper.app.util.ImageUtil;

@Configuration
@Controller
@RequestMapping(value = "/drugStopper/api/")
@PropertySource("file:"+Constants.IMAGE_PROPERTY_LOC)
public class ImagesApi extends RestResource{
	private  String UPLOADED_FOLDER = Constants.STATIC_IMAGE_LOC;
	@Autowired
    private Environment env;
	@RequestMapping(value = "/v1.0/getImagesName", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public List<Image> getImageDescList(HttpServletRequest request) throws IOException, URISyntaxException  {
		File[] files;
		List<Image> imageNameList=new ArrayList<>();
		FileFilter swingFilter = new FileNameExtensionFilter("jpeg files", "jpg","jpeg");
		java.io.FileFilter ioFilter = file -> swingFilter.accept(file);
		files=new File(UPLOADED_FOLDER).listFiles(ioFilter); 
		for (File file : files) {
			if(env.containsProperty(file.getName()))
				imageNameList.add(new Image(file.getName(),env.getProperty(file.getName())));
			else
				imageNameList.add(new Image(file.getName(), env.getProperty("default")));
		}
		return imageNameList;
	}
	
	@RequestMapping(value = "/uploadImage", produces={"application/json"},
			method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String,Object> singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("desc") String desc) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			if (!isUserAdmin()) {
				jsonResponse.setStatusCode(ConstantProperty.UNAUTHORIZED);
				jsonResponse.setMessage(ConstantProperty.NOT_RESISTERED_USER);
				return sendResponse(jsonResponse);
			}
			if (file.isEmpty()) {
				jsonResponse.setStatusCode(ConstantProperty.FILE_NOT_EXIST);
				jsonResponse.setMessage("Please select a file to upload");
				return sendResponse(jsonResponse);
			}

			// Get the file and save it somewhere
			String fileName = UPLOADED_FOLDER + file.getOriginalFilename().replaceAll(" ", "");
			byte[] bytes = file.getBytes();
			Path path = Paths.get(fileName);
			if (Files.notExists(Paths.get(fileName))) {
				Files.write(path, bytes);
				ImageUtil.writePropertiesFile(fileName.substring(fileName.lastIndexOf('/') + 1), desc);
				jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
				jsonResponse.setMessage("Successfully uploaded the image");
				return sendResponse(jsonResponse);
			} else {
				jsonResponse.setStatusCode(ConstantProperty.FILE_OVERRIDE_ERROR); 
				jsonResponse.setMessage("Please change the file name");
				return sendResponse(jsonResponse);
			}  
		} catch (Exception e) { 
			e.printStackTrace();
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			try {
				return sendResponse(jsonResponse);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
}
