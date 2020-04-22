package be.vankerkom.transmissionlayer.models.dto.partials;

import lombok.Getter;

@Getter
public class TransmissionSessionStatisticsDataDto {

    private Long downloadedBytes;

    private Integer filesAdded;

    private Long secondsActive;

    private Integer sessionCount;

    private Long uploadedBytes;

}
