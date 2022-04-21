package cl.gjimenez.encuestabackend.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gjimenez.encuestabackend.entities.UserEntity;
import cl.gjimenez.encuestabackend.models.requests.UserRegisterRequestModel;
import cl.gjimenez.encuestabackend.models.responses.UserRest;
import cl.gjimenez.encuestabackend.services.IUserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	IUserService userService;
	
	@PostMapping
	public UserRest createUser(@Valid @RequestBody UserRegisterRequestModel userModel ){
		UserEntity userE = userService.createUser(userModel);
		
		UserRest userRest = new UserRest();
		
		BeanUtils.copyProperties(userE, userRest);
		return userRest;
	}
	
	@GetMapping
	public UserRest getUser(Authentication authentication) {
		 System.out.print(authentication);
		 String userEmail = authentication.getPrincipal().toString();
		 
		 UserEntity user = userService.getUser(userEmail);
		 UserRest userRest = new UserRest();

		 BeanUtils.copyProperties(user, userRest);
		 return userRest;
	}

	
}
