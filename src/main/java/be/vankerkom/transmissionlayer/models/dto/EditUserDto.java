package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserDto {

    private String password;
    private String newPassword;
    private String downloadDirectory;

}
