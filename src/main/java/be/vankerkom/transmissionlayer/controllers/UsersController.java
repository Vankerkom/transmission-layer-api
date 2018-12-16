package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.exceptions.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.dto.NewUserDto;
import be.vankerkom.transmissionlayer.models.dto.UserDetailsDto;
import be.vankerkom.transmissionlayer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDetailsDto> index() {
        return userService.getAll()
                .orElseThrow(() -> new EntityNotFoundException("Cannot fetch users"));
    }

    @GetMapping("/{id}")
    public UserDetailsDto get(@PathVariable final int id) {
        return userService.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity post(@Valid @RequestBody NewUserDto newUser) throws DuplicateException {
        final UserDetailsDto userDetails = userService.create(newUser)
                .orElseThrow(RuntimeException::new);

        final URI createdLocation = linkTo(methodOn(UsersController.class).get(userDetails.getId())).toUri();

        return ResponseEntity.created(createdLocation)
                .body(userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final int id) {
        if (!userService.deleteById(id)) {
            throw new RuntimeException(); // Internal server error.
        }

        return ResponseEntity.noContent().build();
    }

}
