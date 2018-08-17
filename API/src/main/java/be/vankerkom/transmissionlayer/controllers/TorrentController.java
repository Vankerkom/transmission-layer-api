package be.vankerkom.transmissionlayer.controllers;

import be.vankerkom.transmissionlayer.services.TorrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/torrents")
public class TorrentController {

    @Autowired
    private TorrentService torrentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity index() {
        Object results = torrentService.getTorrents();

        if (results != null) {
            return ResponseEntity.ok(results);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
