package be.vankerkom.transmissionlayer.models.dto;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class TransmissionResponse<T> {

    private static final String SUCCESS_RESULT_VALUE = "success";

    @NotNull
    @NotEmpty
    private String result;

    // Optional
    private Integer tag;

    // Optional
    private T arguments;

    public String getResult() {
        return result;
    }

    public boolean isSuccess() {
        if (StringUtils.isNotBlank(result)) {
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
