package umu.tds.hash;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Hash {
	
	private static SecretKey deriveKey(String password, String salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
	
	public static String cifrarContraseña(String contraseña) {
		String masterPassword = "TDS2024";
        String salt = "123";
        SecretKey key;
        String encryptedPassword = null;
        
		try {
			key = deriveKey(masterPassword, salt);    
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encryptedBytes = cipher.doFinal(contraseña.getBytes());
	        encryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return encryptedPassword;
	}
	
	public static String descifrarContraseña(String encryptedPassword) {
	    String masterPassword = "TDS2024";
	    String salt = "123";
	    SecretKey key;
	    String decryptedPassword = null;

	    try {
	        key = deriveKey(masterPassword, salt);
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
	        decryptedPassword = new String(decryptedBytes);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return decryptedPassword;
	}
}
