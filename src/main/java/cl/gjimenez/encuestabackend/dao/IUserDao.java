package cl.gjimenez.encuestabackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cl.gjimenez.encuestabackend.entities.UserEntity;

@Repository
public interface IUserDao extends CrudRepository<UserEntity, Long> {
	
	public UserEntity findByEmail(String email);

}
