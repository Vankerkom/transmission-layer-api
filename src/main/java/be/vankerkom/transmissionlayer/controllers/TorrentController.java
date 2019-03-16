package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.services.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/torrents")
public class TorrentController {

    @Autowired
    private TorrentService torrentService;

    @GetMapping
    public List<TorrentDto> index(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return torrentService.getTorrents(userPrincipal);
    }

    @PostMapping
    public TorrentDto post(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody NewTorrentRequest request) throws DuplicateException {
        return torrentService.addTorrent(userPrincipal, request)
                .orElseThrow(() -> new RuntimeException("Failed to add the torrent."));
    }

}
