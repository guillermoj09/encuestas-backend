package cl.gjimenez.encuestabackend.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import cl.gjimenez.encuestabackend.annotations.UniqueEmail;
import cl.gjimenez.encuestabackend.dao.IUserDao;
import cl.gjimenez.encuestabackend.entities.UserEntity;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
	
	@Autowired
	IUserDao userDao;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		UserEntity user = userDao.findByEmail(value);
		
		if(user == null ) {
			return true;
		}
		return false;
	}

}
