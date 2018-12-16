package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.dto.NewUserDto;
import be.vankerkom.transmissionlayer.models.dto.UserDetailsDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<List<UserDetailsDto>> getAll();

    Optional<UserDetailsDto> findById(int id);

    boolean deleteById(int id);

    Optional<UserDetailsDto> create(NewUserDto newUserDto) throws DuplicateException;

}
