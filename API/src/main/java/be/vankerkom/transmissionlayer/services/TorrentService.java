package be.vankerkom.transmissionlayer.services;

import be.vankerkom.transmissionlayer.models.dto.TransmissionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;

@Service
public class TorrentService {

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private ModelMapper modelMapper;

    public Object getTorrents() {
        final Map<String, Object> arguments = new HashMap<>();

        arguments.put("fields", new String[]{"id", "name", "status", "percentDone"});

        TransmissionResponse response = transmissionService.getResource("torrent-get", arguments);

        if (response.isSuccess()) {
            // TODO Map response.
            return response.getArguments()
                    .get("torrents");
        }

        return null;
    }

}
