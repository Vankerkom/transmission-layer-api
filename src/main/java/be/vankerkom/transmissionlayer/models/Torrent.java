package be.vankerkom.transmissionlayer.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(indexes = {
        @Index(name = "torrent_id_index", columnList = "user_id,id")
})
public class Torrent {

    @Id
    @Column(nullable = false, unique = true)
    private String hash;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @Column(nullable = false)
    private int id;

}
