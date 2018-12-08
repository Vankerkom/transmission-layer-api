package be.vankerkom.transmissionlayer.models.dto.partials;

public class TorrentDataDto {

    private Integer id;

    private String name;

    private String hashString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashString() {
        return hashString;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }

}
