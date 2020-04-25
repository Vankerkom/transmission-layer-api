package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.exceptions.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.EditUserDto;
import be.vankerkom.transmissionlayer.models.dto.NewUserDto;
import be.vankerkom.transmissionlayer.models.dto.UserDetailsDto;
import be.vankerkom.transmissionlayer.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserDetailsDto> index() {
        return userService.getAll()
                .orElseThrow(() -> new EntityNotFoundException("Cannot fetch users"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public UserDetailsDto get(@PathVariable final int id) {
        return userService.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity post(@Valid @RequestBody NewUserDto newUser) throws DuplicateException {
        final UserDetailsDto userDetails = userService.create(newUser)
                .orElseThrow(RuntimeException::new);

        final URI createdLocation = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsersController.class).get(userDetails.getId())).toUri();

        return ResponseEntity.created(createdLocation)
                .body(userDetails);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.user.id")
    @PatchMapping("/{id}")
    public UserDetailsDto edit(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int id,
                               @Valid @RequestBody EditUserDto editUserRequest) {
        return userService.editUser(userPrincipal.getUser(), id, editUserRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final int id) {
        if (!userService.deleteById(id)) {
            throw new RuntimeException(); // Internal server error.
        }

        return ResponseEntity.noContent().build();
    }

}
