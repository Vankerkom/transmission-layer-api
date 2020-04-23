package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TorrentState {

    STOPPED,
    CHECK_QUEUED,
    CHECKING,
    DOWNLOAD_QUEUED,
    DOWNLOADING,
    SEEDING_QUEUED,
    SEEDING;

    @JsonValue
    public int toValue() {
        return ordinal();
    }

    public static TorrentState of(int code) {
        for (TorrentState value : values()) {
            if (value.ordinal() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown activity code: " + code);
    }

}
