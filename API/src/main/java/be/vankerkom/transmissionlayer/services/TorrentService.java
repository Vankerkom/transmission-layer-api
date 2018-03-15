package be.vankerkom.transmissionlayer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TorrentService {

    @Autowired
    private TransmissionService transmissionService;

    public String getTorrents() {
        Map<String, Object> arguments = new HashMap<>();

        arguments.put("fields", new String[]{"id", "name"});

        return transmissionService.getResource("torrent-get", arguments);
    }

}
