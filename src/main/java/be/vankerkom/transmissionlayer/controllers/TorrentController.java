package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.partials.TorrentDto;
import be.vankerkom.transmissionlayer.services.TorrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/torrents")
@RequiredArgsConstructor
public class TorrentController {

    private final TorrentService torrentService;

    @GetMapping
    public List<TorrentDto> index(@AuthenticationPrincipal final UserPrincipal userPrincipal,
                                  @RequestParam(name = "filter", required = false, defaultValue = "all") final String filter) {
        return torrentService.getTorrents(userPrincipal, filter);
    }

    @PostMapping
    public TorrentDto post(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody NewTorrentRequest request) throws DuplicateException {
        return torrentService.addTorrent(userPrincipal, request)
                .orElseThrow(() -> new RuntimeException("Failed to add the torrent."));
    }

    @DeleteMapping("/{torrentId}")
    public void delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int torrentId) {
        torrentService.deleteByUserAndId(userPrincipal.getUser(), torrentId);
    }

}
