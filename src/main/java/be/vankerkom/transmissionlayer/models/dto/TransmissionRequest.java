package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Getter
public class TransmissionRequest<T> {

    @Required
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
