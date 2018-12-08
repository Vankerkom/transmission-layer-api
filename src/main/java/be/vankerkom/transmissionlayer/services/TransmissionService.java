package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.TransmissionResponseGeneric;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDataDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransmissionService {

    List<TorrentDto> getTorrents(List<String> fields);

    List<TorrentDto> getTorrents(List<String> fields, Set<Integer> ids);

    Optional<Object> getSession();

    Optional<SessionStatisticsDto> getSessionStats();

    TransmissionResponseGeneric getResource(String method);

    TransmissionResponseGeneric getResource(String method, Object arguments);

    Optional<TorrentDataDto> addTorrent(NewTorrentRequest request) throws DuplicateException;

}
