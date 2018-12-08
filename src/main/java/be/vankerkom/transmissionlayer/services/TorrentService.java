package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;

import java.util.List;
import java.util.Optional;

public interface TorrentService {

    Optional<List<TorrentDto>> getTorrents(UserPrincipal userPrincipal);

    Optional<TorrentDto> addTorrent(UserPrincipal userPrincipal, NewTorrentRequest request);

}
