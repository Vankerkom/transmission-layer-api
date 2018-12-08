package be.vankerkom.transmissionlayer.models.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class TransmissionResponse<T> {

    private final static String SUCCESS_RESULT_VALUE = "success";

    @Required
    private String result;

    // Optional
    private Integer tag;

    // Optional
    private T arguments;

    public String getResult() {
        return result;
    }

    public boolean isSuccess() {
        if (!StringUtils.isEmpty(result)) {
            return result.equalsIgnoreCase(SUCCESS_RESULT_VALUE);
        }

        return false;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Optional<Integer> getTag() {
        return Optional.of(tag);
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public T getArguments() {
        return arguments;
    }

    public void setArguments(T arguments) {
        this.arguments = arguments;
    }

}
