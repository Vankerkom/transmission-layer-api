package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDto extends UserDto {

    private String downloadDirectory;

}
