package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TorrentServiceImpl implements TorrentService {

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private ModelMapper modelMapper;

    public List<TorrentDto> getTorrents() {

        final List<String> fields = Arrays.asList("id", "name", "status", "percentDone");

        return transmissionService.getTorrents(fields);
    }

}
