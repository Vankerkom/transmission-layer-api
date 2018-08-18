package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionResponseGeneric;
import be.vankerkom.transmissionlayer.models.dto.partials.SessionStatisticsDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;

import java.util.List;
import java.util.Set;

public interface TransmissionService {

    List<TorrentDto> getTorrents(List<String> fields);

    List<TorrentDto> getTorrents(List<String> fields, Set<Integer> ids);

    Object getSession();

    SessionStatisticsDto getSessionStats();

    TransmissionResponseGeneric getResource(String method);

    TransmissionResponseGeneric getResource(String method, Object arguments);

}
