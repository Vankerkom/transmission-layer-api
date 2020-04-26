package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class TransmissionRequest<T> {

    @NotNull
    @NotEmpty
    private String method;

    // Optional
    private T arguments;

    // Optional
    private Integer tag;

    public TransmissionRequest(final String method, final T arguments) {
        this.method = method;
        this.arguments = arguments;
    }

}
