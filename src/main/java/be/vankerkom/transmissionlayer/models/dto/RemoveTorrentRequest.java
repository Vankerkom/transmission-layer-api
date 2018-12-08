package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoveTorrentRequest {

    @NotNull
    @JsonProperty("ids")
    private Set<Integer> ids;

    @JsonProperty("delete-local-data")
    private Boolean deleteLocalData;

    private RemoveTorrentRequest() {
        this.ids = Collections.emptySet();
    }

    public RemoveTorrentRequest(final Set<Integer> ids, final Boolean deleteLocalData) {
        this();

        setIds(ids);
        setDeleteLocalData(deleteLocalData);
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public void setIds(final Set<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("Ids cannot be empty");
        }

        this.ids = ids;
    }

    public Boolean getDeleteLocalData() {
        return deleteLocalData;
    }

    public void setDeleteLocalData(final Boolean deleteLocalData) {
        this.deleteLocalData = deleteLocalData;
    }

}
