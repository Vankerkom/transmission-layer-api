package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.exceptions.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.EditUserDto;
import be.vankerkom.transmissionlayer.models.dto.NewUserDto;
import be.vankerkom.transmissionlayer.models.dto.UserDetailsDto;
import be.vankerkom.transmissionlayer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<List<UserDetailsDto>> getAll() {
        try {
            final Iterable<User> users = userRepository.findAll();

            final List<UserDetailsDto> userDetailsList = StreamSupport.stream(users.spliterator(), true)
                    .map(u -> mapper.map(u, UserDetailsDto.class))
                    .collect(Collectors.toList());

            return Optional.of(userDetailsList);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDetailsDto> findById(final int id) {
        return userRepository.findById(id)
                .map(u -> mapper.map(u, UserDetailsDto.class));
    }

    public boolean deleteById(final int id) {
        final User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            log.error("Could not delete userId: {}", user.getId(), e);
            return false;
        }
    }

    public Optional<UserDetailsDto> create(final NewUserDto newUserDto) throws DuplicateException {
        final String username = newUserDto.getUsername();
        final Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            throw new DuplicateException("A user with username: \'" + username + "\' already exists");
        }

        try {
            final User inputUser = mapper.map(newUserDto, User.class);

            // We don't want to store the user's password as plain text...
            inputUser.setPassword(passwordEncoder.encode(inputUser.getPassword()));

            final User savedUser = userRepository.save(inputUser);
            final UserDetailsDto mappedUser = mapper.map(savedUser, UserDetailsDto.class);

            return Optional.ofNullable(mappedUser);
        } catch (Exception e) {
            log.error("Failed to create user", e);
        }

        return Optional.empty();
    }

    public UserDetailsDto editUser(final int id, final EditUserDto editUserRequest) {
        final User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        editUserRequest.getDownloadDirectory()
                .ifPresent(user::setDownloadDirectory);

        return mapper.map(userRepository.save(user), UserDetailsDto.class);
    }

}
