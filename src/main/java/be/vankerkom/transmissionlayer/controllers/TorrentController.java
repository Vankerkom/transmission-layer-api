package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.EntityNotFoundException;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.services.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/torrents")
public class TorrentController {

    @Autowired
    private TorrentService torrentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TorrentDto> index(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return torrentService.getTorrents(userPrincipal)
                .orElseThrow(() -> new EntityNotFoundException("Could not fetch torrent data."));
    }

}
