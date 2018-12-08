package be.vankerkom.transmissionlayer.models.dto;

import javax.validation.constraints.NotBlank;

public class NewTorrentRequest {

    @NotBlank
    private String fileName;

    private String metaInfo;

    public String getFileName() {
        return fileName;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

}
