package be.vankerkom.transmissionlayer.models.dto;

import java.io.Serializable;
import java.util.Map;

public class TransmissionRequest implements Serializable {

    private String method;

    // Optional
    private Map<String, Object> arguments;

    // Optional
    private Integer tag;

    public TransmissionRequest(String method, Map<String, Object> arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public Integer getTag() {
        return tag;
    }

}
