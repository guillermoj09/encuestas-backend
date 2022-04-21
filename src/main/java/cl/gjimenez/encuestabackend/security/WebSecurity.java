package cl.gjimenez.encuestabackend.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cl.gjimenez.encuestabackend.services.IUserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter  {
	
	private final IUserService userService;
	
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(IUserService userService,BCryptPasswordEncoder bcryp) {
		this.bCryptPasswordEncoder = bcryp;
		
		this.userService = userService;
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.cors().and().csrf().disable();
		
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/users").permitAll().anyRequest().authenticated();
		
		http.addFilter(getAuthenticationFilter())
		.addFilter(new AuthorizationFilter(authenticationManager()))
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	
	}
	
	public AuthenticationFilter getAuthenticationFilter() throws Exception{
		final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager());
		
		authenticationFilter.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
		
		return authenticationFilter;
	}
}
