package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ModelMapper mapper;

    @GetMapping
    public UserDto index(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return mapper.map(userPrincipal.getUser(), UserDto.class);
    }

}
