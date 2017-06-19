package com.udelblue.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptUtil {

	private Key aesKey;

	private Cipher cipher;
	String key = "Bar98345Boo11845";
	final Logger log = LoggerFactory.getLogger(EncryptUtil.class);

	public EncryptUtil() throws Exception {
		aesKey = new SecretKeySpec(key.getBytes(), "AES");
		cipher = Cipher.getInstance("AES");
	}

	public String decrypt(String encryptedString) {
		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			String decrypted = new String(cipher.doFinal(encryptedString.getBytes()));
			decryptedText = new String(decrypted);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return decryptedText;
	}

	public String decryptEncoded(String encryptedString) {
		String decryptedText = null;
		try {

			byte[] decodedbytes = Base64.decodeBase64(encryptedString.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decryptedbytes = cipher.doFinal(decodedbytes);

			decryptedText = new String(decryptedbytes);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return decryptedText;
	}

	public String encrypt(String unencryptedString) {
		String encryptedString = null;
		try {

			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(unencryptedString.getBytes());

			encryptedString = new String(encrypted);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return encryptedString;
	}

	public String encryptEncoded(String unencryptedString) {
		String encryptedString = null;
		try {

			byte[] bytes = unencryptedString.getBytes();
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(bytes);
			// encode

			byte[] encryptedbyte = Base64.encodeBase64(encrypted);
			String lencryptedString = new String(encryptedbyte);
			// request param would remove + char
			encryptedString = lencryptedString.replace("+", "%2B");

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return encryptedString;
	}

}