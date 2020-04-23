package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.exceptions.DuplicateException;
import be.vankerkom.transmissionlayer.models.UserPrincipal;
import be.vankerkom.transmissionlayer.models.dto.NewTorrentRequest;
import be.vankerkom.transmissionlayer.models.dto.api.TorrentDto;
import be.vankerkom.transmissionlayer.models.dto.partials.TransmissionTorrentDataDto;
import be.vankerkom.transmissionlayer.services.TorrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@RestController
@RequestMapping("/api/torrents")
@RequiredArgsConstructor
public class TorrentController {

    private final TorrentService torrentService;

    @GetMapping
    public List<TorrentDto> index(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                  @RequestParam(required = false, defaultValue = "all") String filter) {
        return torrentService.getTorrents(userPrincipal, filter)
                .stream()
                .map(TorrentDto::of)
                .collect(toUnmodifiableList());
    }

    @PostMapping
    public TransmissionTorrentDataDto post(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @Valid @RequestBody NewTorrentRequest request) throws DuplicateException {
        return torrentService.addTorrent(userPrincipal, request)
                .orElseThrow(() -> new RuntimeException("Failed to add the torrent."));
    }

    @DeleteMapping("/{torrentHash}")
    public void delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String torrentHash) {
        torrentService.deleteByUserAndId(userPrincipal.getUser(), torrentHash);
    }

}
