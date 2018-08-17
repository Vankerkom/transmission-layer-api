package be.vankerkom.transmissionlayer.models.dto.partials;

public class TorrentDto {

    private Integer id;

    private String name;

    private Integer status;

    private Float percentDone;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public Float getPercentDone() {
        return percentDone;
    }

}
