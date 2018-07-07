package com.drugstopper.app.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drugstopper.app.bean.Image;

@Controller
@RequestMapping(value = "/drugStopper")
public class GetImages {
	@RequestMapping(value = "/v1.0/getImagesName", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public List<Image> checkDb(HttpServletRequest request) throws IOException  {
		File[] files;
		List<Image> imageNameList=new ArrayList<>();
		FileFilter swingFilter = new FileNameExtensionFilter("jpeg files", "jpg","jpeg");
		java.io.FileFilter ioFilter = file -> swingFilter.accept(file);
		files=new File("src/main/resources/static/DrugStopperHomePageImages").listFiles(ioFilter);
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("src/main/resources/imageDescription.properties");
		prop.load(input);
		for (File file : files) {
			if(prop.containsKey(file.getName()))
				imageNameList.add(new Image(file.getName(),prop.getProperty(file.getName())));
			else
				imageNameList.add(new Image(file.getName(), prop.getProperty("default")));
		}
		return imageNameList;
	}
}
