package com.explorer.encryption;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class EncryptDecrypt {

	public static void main(String[] args) throws Exception {
		 InputStream keystoreStream = new FileInputStream("C:\\keystore\\keystore.jceks");
		  KeyStore keystore = KeyStore.getInstance("JCEKS");
		  keystore.load(keystoreStream, "storepass".toCharArray());

		  if (!keystore.containsAlias("key1")) {
		   throw new RuntimeException("Alias for key not found");
		  }
		  Key key = keystore.getKey("key1", "keypass".toCharArray());
		  SecretKeySpec secretKeySpecification = new SecretKeySpec(key.getEncoded(), "AES");
		  IvParameterSpec iv = new IvParameterSpec("abcdef".getBytes("UTF-8"));

		  String encryptText = "admin:password";

		  String encryptedText = encryptMessage(encryptText, secretKeySpecification, iv);
		  System.out.println("encryptedOne:" + encryptedText);

		  String decryptedText = decryptMessage(encryptedText, secretKeySpecification, iv);
		  System.out.println("decrypted :" + decryptedText);

	}
	
	 private static String encryptMessage(String message, SecretKeySpec spec, IvParameterSpec iv) throws Exception {

		  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		  cipher.init(Cipher.ENCRYPT_MODE, spec, iv);
		  // 1.encode
		  byte[] encodedMessage = Base64.getEncoder().encode(message.getBytes("UTF-8"));
		  // encrypt
		  byte[] encryptedMessageInBytes = cipher.doFinal(encodedMessage);
		  // encode
		  byte[] base64EncodedEncryptedMsg = Base64.getEncoder().encode(encryptedMessageInBytes);
		  return new String(base64EncodedEncryptedMsg);
		 }

		 private static String decryptMessage(String encryptedString, SecretKeySpec spec, IvParameterSpec iv)
		   throws Exception {
		  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		  cipher.init(Cipher.DECRYPT_MODE, spec, iv);
		  // 1. decode
		  byte[] decodedEncryptedMessage = Base64.getDecoder().decode(encryptedString.getBytes());
		  // 2. decrypt
		  byte[] decryptedMessageInBytes = cipher.doFinal(decodedEncryptedMessage);
		  // decode
		  return new String(Base64.getDecoder().decode(decryptedMessageInBytes));
		 }

}
