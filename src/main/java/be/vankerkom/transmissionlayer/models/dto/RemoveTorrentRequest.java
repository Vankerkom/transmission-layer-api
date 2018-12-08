package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoveTorrentRequest extends IdsRequest {

    @JsonProperty("delete-local-data")
    private Boolean deleteLocalData;

    public RemoveTorrentRequest(final Set<Integer> ids, final Boolean deleteLocalData) {
        super(ids);

        setDeleteLocalData(deleteLocalData);
    }

    public Boolean getDeleteLocalData() {
        return deleteLocalData;
    }

    public void setDeleteLocalData(final Boolean deleteLocalData) {
        this.deleteLocalData = deleteLocalData;
    }

}
