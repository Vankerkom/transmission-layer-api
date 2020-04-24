package be.vankerkom.transmissionlayer.models.dto.api;

import be.vankerkom.transmissionlayer.models.dto.TorrentState;
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
    public final String hashString;
    public final TorrentState state;
    public final int progress;
    public final boolean finished;
    public final long size;
    public final long rx;
    public final long tx;
    public final long rxRate;
    public final long txRate;

    public static TorrentDto of(TransmissionTorrentDto torrent) {
        return TorrentDto.builder()
                .id(torrent.getId())
                .hashString(torrent.getHashString())
                .name(torrent.getName())
                .state(torrent.getStatus())
                .progress(min(round(torrent.getPercentDone() * 100), 100))
                .finished(torrent.isFinished())
                .rx(torrent.getDownloadedEver())
                .tx(torrent.getUploadedEver())
                .rxRate(torrent.getRateDownload())
                .txRate(torrent.getRateUpload())
                .size(torrent.getTotalSize())
                .build();
    }

}
