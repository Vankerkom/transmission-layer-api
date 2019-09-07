package be.vankerkom.transmissionlayer.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Torrent {

    @Id
    @Column(nullable = false, unique = true)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

}
