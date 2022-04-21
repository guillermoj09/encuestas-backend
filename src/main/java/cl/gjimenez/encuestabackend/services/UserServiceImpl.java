package cl.gjimenez.encuestabackend.services;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cl.gjimenez.encuestabackend.dao.IUserDao;
import cl.gjimenez.encuestabackend.entities.UserEntity;
import cl.gjimenez.encuestabackend.models.requests.UserRegisterRequestModel;

@Service	
public class UserServiceImpl implements IUserService {
	
	//@Autowired otra forma de hacer la inyeccion de dependencia
	//IUserDao userDao;
	IUserDao userDao;
	
	BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(IUserDao userDao,BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userDao = userDao;
	}
	
	@Override
	public UserEntity createUser(UserRegisterRequestModel user) {
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		return userDao.save(userEntity); 
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userDao.findByEmail(email);
		
		if(userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserEntity getUser(String email) {
		UserEntity userEntity = userDao.findByEmail(email);
		return userEntity;
	}
	

}
