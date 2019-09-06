package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AddTorrentRequest {

    @JsonProperty("cookies")
    private String cookies;

    @JsonProperty("download-dir")
    private String downloadDirectory;

    @JsonProperty("filename")
    private String fileName;

    @JsonProperty("metainfo")
    private String metaInfo;

    @JsonProperty("paused")
    private Boolean paused;

    @JsonProperty("peer-limit")
    private Integer peerLimit;

    private Integer bandwidthPriority;

    @JsonProperty("files-wanted")
    private Set<Integer> wantedFiles;

    @JsonProperty("files-unwanted")
    private Set<Integer> unwantedFiles;

    @JsonProperty("priority-high")
    private Set<Integer> highPriority;

    @JsonProperty("priority-low")
    private Set<Integer> lowPriority;

    @JsonProperty("priority-normal")
    private Set<Integer> normalPriority;

}
