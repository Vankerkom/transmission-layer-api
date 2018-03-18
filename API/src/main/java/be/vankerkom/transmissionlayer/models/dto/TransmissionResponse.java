package be.vankerkom.transmissionlayer.models.dto;

import java.io.Serializable;
import java.util.Map;

public class TransmissionResponse implements Serializable {

    private String result;

    // Optional
    private Map<String, Object> arguments;

    // Optional
    private int tag;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
