package be.vankerkom.transmissionlayer.repositories;

import be.vankerkom.transmissionlayer.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUsernameIgnoreCase(String username);

}
