package be.vankerkom.transmissionlayer.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Torrent {

    @Id
    @Column(nullable = false, unique = true)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    protected Torrent() {
    }

    public Torrent(final Integer id, final User user) {
        this.id = id;
        this.setUser(user);
    }

    private void setUser(final User user) {
        this.user = user;
        if (user != null) {
            user.getTorrents().add(this);
        }
    }

    public Integer getId() {
        return id;
    }

}
