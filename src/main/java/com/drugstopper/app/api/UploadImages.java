package com.drugstopper.app.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/drugStopper/uploadImage")
public class UploadImages {
	private static String UPLOADED_FOLDER = "src/main/resources/static/DrugStopperHomePageImages/";

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("desc") String desc,
			RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {

			// Get the file and save it somewhere
			String fileName=UPLOADED_FOLDER + file.getOriginalFilename().replaceAll(" ", "");
			byte[] bytes = file.getBytes();
			Path path = Paths.get(fileName);
			if(Files.notExists(Paths.get(fileName))) {
				Files.write(path, bytes);
				redirectAttributes.addFlashAttribute("message",
						"You successfully uploaded '" + file.getOriginalFilename().replaceAll(" ", "") + "'");
				writePropertiesFile(fileName.substring(fileName.lastIndexOf('/')+1), desc);
			}
			else {
				redirectAttributes.addFlashAttribute("error", "Please change the file name");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
	public void writePropertiesFile(String key, String data) {
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try {
            Properties configProperty = new Properties();

            File file = new File("src/main/resources/imageDescription.properties");
            fileIn = new FileInputStream(file);
            configProperty.load(fileIn);
            configProperty.setProperty(key, data);
            fileOut = new FileOutputStream(file);
            configProperty.store(fileOut, "sample properties");

        } catch (Exception ex) {
        	System.out.println(ex);
        } finally {

            try {
                fileOut.close();
            } catch (IOException ex) {
            	System.out.println(ex);
            }
        }
    }
}
