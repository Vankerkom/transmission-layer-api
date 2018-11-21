package be.vankerkom.transmissionlayer.models.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

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

    public String getMethod() {
        return method;
    }

    public T getArguments() {
        return arguments;
    }

    public Integer getTag() {
        return tag;
    }

}
