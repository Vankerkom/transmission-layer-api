package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTorrentsRequest {

    @NotNull
    @JsonProperty("fields")
    private List<String> fields;

    @JsonProperty("ids")
    private List<Integer> ids;

    public GetTorrentsRequest() {
       this.fields = Collections.singletonList("id");
    }

    public GetTorrentsRequest(@NotNull final List<String> fields) {
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(@NotNull final List<String> fields) {
        this.fields = fields;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

}
