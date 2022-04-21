package cl.gjimenez.encuestabackend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import cl.gjimenez.encuestabackend.entities.UserEntity;
import cl.gjimenez.encuestabackend.models.requests.UserRegisterRequestModel;

public interface IUserService extends UserDetailsService {
	
	public UserDetails loadUserByUsername(String email);
	
	public UserEntity createUser(UserRegisterRequestModel user);

	public UserEntity getUser(String email);
}
