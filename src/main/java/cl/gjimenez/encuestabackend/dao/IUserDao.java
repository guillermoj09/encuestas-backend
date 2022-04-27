package cl.gjimenez.encuestabackend.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cl.gjimenez.encuestabackend.entities.UserEntity;

@Repository
public interface IUserDao extends CrudRepository<UserEntity, Long> {
	
	public UserEntity findByEmail(String email);
	
	public Optional<UserEntity> findById(Long id);

}
