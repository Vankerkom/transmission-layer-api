package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;

import java.util.List;

public interface TorrentService {

    List<TorrentDto> getTorrents();

}
