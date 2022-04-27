package cl.gjimenez.encuestabackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;

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

import cl.gjimenez.encuestabackend.dao.IUserDao;
import cl.gjimenez.encuestabackend.entities.UserEntity;
import cl.gjimenez.encuestabackend.models.requests.UserLoginRequetsModel;
import cl.gjimenez.encuestabackend.models.requests.UserRegisterRequestModel;
import cl.gjimenez.encuestabackend.models.responses.UserRest;
import cl.gjimenez.encuestabackend.models.responses.ValidationErrors;
import cl.gjimenez.encuestabackend.services.IUserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTests {
	
	private static final  String API_URL = "/users";
	
	@Autowired
	TestRestTemplate template;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IUserDao userDao;
	
	@BeforeEach
	public void cleanup() {
		userDao.deleteAll();
	}
	
	@Test 
	public void createUser_sinNingunDatoRetornaBadRequest() {
		
		ResponseEntity<Object> response = register(new UserRegisterRequestModel(), Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test 
	public void createUser_sinElCampoNombreRetornaBadRequest() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		user.setName(null);
		ResponseEntity<Object> response = register(user, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	

	@Test 
	public void createUser_sinElCampoPasswordRetornaBadRequest() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		user.setPassword(null);
		ResponseEntity<Object> response = register(user, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	@Test 
	public void createUser_sinElCampoEMailRetornaBadRequest() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		user.setEmail(null);
		ResponseEntity<Object> response = register(user, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test 
	public void createUser_sinNingunDatoRetornaErroresDeValidcaion() {
		
		ResponseEntity<ValidationErrors> response = register(new UserRegisterRequestModel(), ValidationErrors.class);
		
		Map<String,String> errors = response.getBody().getErrors();
		assertEquals(errors.size(), 3);
	}
	
	@Test 
	public void createUser_sinNombre_RetornaMensajeErroresDeValidcaionParaElNombre() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		user.setName(null);
		ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
		
		Map<String,String> errors = response.getBody().getErrors();
		assertTrue(errors.containsKey("name"));
	}
	
	@Test 
	public void createUser_sinEmail_RetornaMensajeErroresDeValidcaionParaElNombre() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		user.setEmail(null);
		ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
		
		Map<String,String> errors = response.getBody().getErrors();
		assertTrue(errors.containsKey("email"));
	}
	
	@Test 
	public void createUser_sinPassword_RetornaMensajeErroresDeValidcaionParaElNombre() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		user.setPassword(null);
		ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
		
		Map<String,String> errors = response.getBody().getErrors();
		assertTrue(errors.containsKey("password"));
	}
	
	@Test 
	public void createUser_conUsuarioValido_Retorna() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		
		ResponseEntity<UserRest> response = register(user, UserRest.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test 
	public void createUser_conUsuarioValido_RetornaUserRest() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		
		ResponseEntity<UserRest> response = register(user, UserRest.class);
		
		assertEquals(response.getBody().getName(), user.getName());
	}
	
	@Test 
	public void createUser_conUsuarioValido_guardarUsuarioEnBd() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		
		ResponseEntity<UserRest> response = register(user, UserRest.class);
		
		Optional<UserEntity> userEntity = userDao.findById(response.getBody().getId());
		
		assertNotNull(userEntity);
	}
	
	@Test 
	public void createUser_conUsuarioValido_guardarElpasswordHashEnBd() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		
		ResponseEntity<UserRest> response = register(user, UserRest.class);
		
		Optional<UserEntity> userEntity = userDao.findById(response.getBody().getId());
		
		assertNotEquals(user.getPassword(),userEntity.get().getEncryptedPassword());
	}
	
	
	@Test 
	public void createUser_conUsuarioValidoConCorreoExistente_RetornaMensajeDeErrorParaEmail() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		
		register(user, UserRest.class);
		
		ResponseEntity<ValidationErrors> response2 = register(user, ValidationErrors.class);
		
		
		
		Map<String,String> errors = response2.getBody().getErrors();
		
		assertTrue(errors.containsKey("email"));
	}
	
	@Test 
	public void getUser_sinAuthToken_retornaForbidden() {

		ResponseEntity<Object> response = getUser(null,new ParameterizedTypeReference<Object>(){});
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test 
	public void getUser_conAuthToken_retornaUserRest() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		userService.createUser(user);
		
		UserLoginRequetsModel model = new UserLoginRequetsModel();
		model.setEmail(user.getEmail());
		model.setPassword(user.getPassword());
		ResponseEntity<Map<String, String>> responseLogin = login(model, new ParameterizedTypeReference<Map<String,String>>(){});
		Map<String, String> body = responseLogin.getBody();
		
		String token = body.get("token").replace("Bearer", "");
		
		ResponseEntity<UserRest> response = getUser(token,new ParameterizedTypeReference<UserRest>(){});
		assertEquals(user.getName(), response.getBody().getName());
	}
	
	@Test 
	public void getUser_conAuthToken_retornaOK() {
		UserRegisterRequestModel user = TestUtil.createValidUser();
		userService.createUser(user);
		
		UserLoginRequetsModel model = new UserLoginRequetsModel();
		model.setEmail(user.getEmail());
		model.setPassword(user.getPassword());
		ResponseEntity<Map<String, String>> responseLogin = login(model, new ParameterizedTypeReference<Map<String,String>>(){});
		Map<String, String> body = responseLogin.getBody();
		
		String token = body.get("token").replace("Bearer", "");
		
		ResponseEntity<UserRest> response = getUser(token,new ParameterizedTypeReference<UserRest>(){});
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	
	
	
	
	public <T> ResponseEntity<T> register(UserRegisterRequestModel data, Class<T> responseType){
		return template.postForEntity(API_URL, data, responseType);
		
	}
	
	public <T> ResponseEntity<T> getUser(String token,ParameterizedTypeReference<T> responseType){
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Object> entity = new HttpEntity<Object>(null,headers);
		return template.exchange(API_URL, HttpMethod.GET,entity,responseType);
	}
	
	
	public <T> ResponseEntity<T> login(UserLoginRequetsModel data, ParameterizedTypeReference<T> responseType){
		HttpEntity<UserLoginRequetsModel> entity = new HttpEntity<UserLoginRequetsModel>(data,new HttpHeaders());
		return template.exchange(TestUtil.API_LOGIN_URL, HttpMethod.POST,entity,responseType);
	}
	
	
}
