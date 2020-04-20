package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class EditUserDto {

    private Optional<String> downloadDirectory;

}
