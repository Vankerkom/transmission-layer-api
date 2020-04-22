package be.vankerkom.transmissionlayer.models.dto.api;

import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDto;
import lombok.Builder;
import lombok.ToString;

import static java.lang.Math.min;
import static java.lang.Math.round;

@ToString
@Builder
public class TorrentDto {

    public final int id;
    public final String name;
    public final String hash;
    public final String status;
    public final int progress;
    public final boolean finished;
    public final int rx;
    public final int tx;

    public static TorrentDto of(TransmissionTorrentDto torrent) {
        return TorrentDto.builder()
                .id(0)
                .hash(torrent.getHashString())
                .name(torrent.getName())
                .status("WIP")
                .progress(min(round(torrent.getPercentDone() * 100), 100))
                .finished(torrent.isFinished())
                .build();
    }

}
