package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.services.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/torrents")
public class TorrentController {

    @Autowired
    private TorrentService torrentService;

    @GetMapping(produces = "application/json")
    public String index() {
        return torrentService.getTorrents();
    }

}
