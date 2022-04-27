package cl.gjimenez.encuestabackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.yaml.snakeyaml.extensions.compactnotation.PackageCompactConstructor;

import cl.gjimenez.encuestabackend.dao.IUserDao;
import cl.gjimenez.encuestabackend.models.requests.UserLoginRequetsModel;
import cl.gjimenez.encuestabackend.models.requests.UserRegisterRequestModel;
import cl.gjimenez.encuestabackend.services.IUserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginTests {
	
	private static final String API_LOGIN_URL = "/users/login";
	
	@Autowired
	TestRestTemplate testRestTemplate; 
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IUserService userService;
	
	@BeforeEach
	public void  cleanup() {
		userDao.deleteAll();
		
	}
	
	@Test
	public void postLogin_sinCredenciales_retornaFobidden() {
		ResponseEntity<Object> response = login(null,Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void postLogin_conCredencialesIncorrectas_retornaUnauthorized() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		userService.createUser(user);
		
		UserLoginRequetsModel model = new UserLoginRequetsModel();
		model.setEmail("sadad@gmail.com");
		model.setPassword("123123213");
		ResponseEntity<Object> response = login(null,Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void postLogin_conCredencialesCorrectas_retornaOK() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		userService.createUser(user);
		
		UserLoginRequetsModel model = new UserLoginRequetsModel();
		model.setEmail(user.getEmail());
		model.setPassword(user.getPassword());
		ResponseEntity<Object> response = login(model,Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void postLogin_conCredencialesCorrectas_retornaAuthToken() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		userService.createUser(user);
		
		UserLoginRequetsModel model = new UserLoginRequetsModel();
		model.setEmail(user.getEmail());
		model.setPassword(user.getPassword());
		ResponseEntity<Map<String, String>> response = login(model, new ParameterizedTypeReference<Map<String,String>>(){});
		Map<String,String> body = response.getBody();
		
		String token = body.get("token");
		
		assertTrue(token.contains("Bearer"));
	}
	public <T> ResponseEntity<T> login(UserLoginRequetsModel data, Class<T> responseType){
		return testRestTemplate.postForEntity(API_LOGIN_URL, data, responseType);
		
	}
	
	public <T> ResponseEntity<T> login(UserLoginRequetsModel data, ParameterizedTypeReference<T> responseType){
		HttpEntity<UserLoginRequetsModel> entity = new HttpEntity<UserLoginRequetsModel>(data,new HttpHeaders());
		return testRestTemplate.exchange(API_LOGIN_URL, HttpMethod.POST,entity,responseType);
	}


}
