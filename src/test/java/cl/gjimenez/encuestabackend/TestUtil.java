package cl.gjimenez.encuestabackend;

import java.util.Random;

import cl.gjimenez.encuestabackend.models.requests.UserRegisterRequestModel;

public class TestUtil {
	
	public final static String API_LOGIN_URL = "/users/login";
	
	public static UserRegisterRequestModel createValidUser() {
		UserRegisterRequestModel userRegister = new UserRegisterRequestModel();
		userRegister.setEmail(generateRandomString(16)+"@gmail.com");
		userRegister.setName(generateRandomString(8));
		userRegister.setPassword(generateRandomString(8));
		return userRegister;
		
	}
	
	public static String generateRandomString(int len) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();
		
		StringBuilder sb = new StringBuilder(len);
		
		for (int i = 0; i < len ; i++) {
			sb.append(chars.charAt(rnd.nextInt(chars.length())));
		}
		return sb.toString();
		
	}
}
