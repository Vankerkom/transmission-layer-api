package be.vankerkom.transmissionlayer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "torrent_id_index", columnList = "user_id,id")
})
public class Torrent {

    @Id
    @Column(nullable = false, unique = true)
    private String hash;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ToString.Exclude()
    private User user;

    @NotNull
    @Column(nullable = false)
    private int id;

}
