package cl.gjimenez.encuestabackend.models.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import cl.gjimenez.encuestabackend.annotations.UniqueEmail;

public class UserRegisterRequestModel {
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	@Email
	@UniqueEmail
	private String email;

	@NotEmpty
	@Size(min = 8, max = 40 )
	private String password;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
