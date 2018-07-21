package com.drugstopper.app.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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
@RequestMapping(value = "/drugstopper/api")
public class ImagesApi extends RestResource{
	
	private  String UPLOADED_FOLDER = Constants.STATIC_IMAGE_LOC;
	
	private Class clazz = ImagesApi.class;
	
	private JsonResponse jsonResponse;

	@Autowired
    private Environment env;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/v1.0/getImagesName", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public HashMap<String,Object> getImageDescList(HttpServletRequest request) throws IOException, URISyntaxException, Exception  {
		jsonResponse=new JsonResponse();
		File[] files;
		List<Image> imageNameList=new ArrayList<>();
		FileFilter swingFilter = new FileNameExtensionFilter("jpeg files", "jpg","jpeg");
		Properties props = new Properties();
		FileInputStream input = new FileInputStream(new File(Constants.IMAGE_PROPERTY_LOC));
		props.load(new InputStreamReader(input, Charset.forName("UTF-8")));

		try {
		java.io.FileFilter ioFilter = file -> swingFilter.accept(file);
		files=new File(UPLOADED_FOLDER).listFiles(ioFilter); 
		for (File file : files) 
			imageNameList.add(new Image(file.getName(),props.getProperty(file.getName() , env.getProperty("default"))));
		} catch(Exception ex) {
			jsonResponse.setStatusCode(ConstantProperty.SERVER_ERROR);
			jsonResponse.setMessage(ConstantProperty.INTERNAL_SERVER_ERROR);
			log(clazz, ex.getMessage(), ConstantProperty.LOG_ERROR);
			return sendResponse(jsonResponse);
		}
		jsonResponse.setStatusCode(ConstantProperty.OK_STATUS);
		jsonResponse.setMessage(ConstantProperty.SUCCESSFUL_SAVED);
		Collections.sort(imageNameList);
		jsonResponse.setImageList(imageNameList);
		return sendResponse(jsonResponse);
		
	}
}
