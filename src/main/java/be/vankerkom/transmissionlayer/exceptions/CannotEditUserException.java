package be.vankerkom.transmissionlayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotEditUserException extends RuntimeException {

    public CannotEditUserException(int id) {
        super("Cannot edit user with id: " + id);
    }

}
