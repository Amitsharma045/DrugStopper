package com.drugstopper.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ImageUtil {
	@Autowired
	public static void writePropertiesFile(String key, String data) {
        FileOutputStream fileOut = null;
        FileInputStream fileIn = null;
        try {
            Properties configProperty = new Properties();
            File file = new File(Constants.IMAGE_PROPERTY_LOC);
            fileIn = new FileInputStream(file);
            configProperty.load(new InputStreamReader(fileIn, Charset.forName("UTF-8")));
            configProperty.setProperty(key, data);
            fileOut = new FileOutputStream(file);
            configProperty.store(new OutputStreamWriter(fileOut, "UTF-8"), "sample properties");
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
