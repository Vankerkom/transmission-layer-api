package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class IdsRequest {

    @NotNull
    @JsonProperty("ids")
    private Set<Integer> ids;

    public IdsRequest(final Set<Integer> ids) {
        setIds(ids);
    }

    public Set<Integer> getIds() {
        return ids;
    }

    void setIds(final Set<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        this.ids = ids;
    }

}
