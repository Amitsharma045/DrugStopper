package com.drugstopper.app.util;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

/**
 * @author rpsingh
 *
 */
public class AESEncryptionAlgo {
	
	private static String secretKeyAlgo = "FuckDrug_DrugFreeState";
	private static String algorithm = "AES";
	 public static String encrypt(String plainText) throws Exception 
	    {
		 		SecretKeySpec key = generateKey();
	            Cipher chiper = Cipher.getInstance(algorithm);
	            chiper.init(Cipher.ENCRYPT_MODE, key);
	            byte[] encVal = chiper.doFinal(plainText.getBytes());
	            String encryptedValue = new BASE64Encoder().encode(encVal);
	            return encryptedValue;
	    }

	    // Performs decryption
	    public static String decrypt(String encryptedText) throws Exception 
	    {
	            // generate key 
	    		SecretKeySpec key = generateKey();
	            Cipher chiper = Cipher.getInstance(algorithm);
	            chiper.init(Cipher.DECRYPT_MODE, key);
	            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
	            byte[] decValue = chiper.doFinal(decordedValue);
	            String decryptedValue = new String(decValue);
	            return decryptedValue;
	    }

	//generateKey() is used to generate a secret key for AES algorithm
	    private static SecretKeySpec generateKey() throws Exception 
	    {	
	    	byte[] key = secretKeyAlgo.getBytes("UTF-8");
	    	MessageDigest sha = MessageDigest.getInstance("SHA-1");
	    	key = sha.digest(key);
	    	key = Arrays.copyOf(key, 16);
	    	SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
	    	return secretKeySpec;
	    }

/*	    // performs encryption & decryption 
	    public static void main(String[] args) throws Exception 
	    {
	    	String plainText = "9781805625";
	    	String encryptedText = AESEncryptionAlgo.encrypt(plainText);
	    	String decryptedText = AESEncryptionAlgo.decrypt(encryptedText);

	    	System.out.println("Plain Text : " + plainText);
	    	System.out.println("Encrypted Text : " + encryptedText);
	    	System.out.println("Decrypted Text : " + decryptedText);
	    }*/
}
