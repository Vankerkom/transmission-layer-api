package be.vankerkom.transmissionlayer.models.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class NewTorrentRequest {

    @NotBlank
    private String fileName;

    private String metaInfo;

}
