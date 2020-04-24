package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StateUpdate {

    START,
    STOP;

    @JsonValue
    public int toValue() {
        return ordinal();
    }

}
