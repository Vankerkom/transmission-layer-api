package be.vankerkom.transmissionlayer.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddTorrentRequest {

    @JsonProperty("cookies")
    private String cookies;

    @JsonProperty("download-dir")
    private String downloadDirectory;

    @JsonProperty("filename")
    private String fileName;

    @JsonProperty("metainfo")
    private String metaInfo;

    @JsonProperty("paused")
    private Boolean paused;

    @JsonProperty("peer-limit")
    private Integer peerLimit;

    private Integer bandwidthPriority;

    @JsonProperty("files-wanted")
    private Set<Integer> wantedFiles;

    @JsonProperty("files-unwanted")
    private Set<Integer> unwantedFiles;

    @JsonProperty("priority-high")
    private Set<Integer> highPriority;

    @JsonProperty("priority-low")
    private Set<Integer> lowPriority;

    @JsonProperty("priority-normal")
    private Set<Integer> normalPriority;

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getDownloadDirectory() {
        return downloadDirectory;
    }

    public void setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    public Integer getPeerLimit() {
        return peerLimit;
    }

    public void setPeerLimit(Integer peerLimit) {
        this.peerLimit = peerLimit;
    }

    public Integer getBandwidthPriority() {
        return bandwidthPriority;
    }

    public void setBandwidthPriority(Integer bandwidthPriority) {
        this.bandwidthPriority = bandwidthPriority;
    }

    public Set<Integer> getWantedFiles() {
        return wantedFiles;
    }

    public void setWantedFiles(Set<Integer> wantedFiles) {
        this.wantedFiles = wantedFiles;
    }

    public Set<Integer> getUnwantedFiles() {
        return unwantedFiles;
    }

    public void setUnwantedFiles(Set<Integer> unwantedFiles) {
        this.unwantedFiles = unwantedFiles;
    }

    public Set<Integer> getHighPriority() {
        return highPriority;
    }

    public void setHighPriority(Set<Integer> highPriority) {
        this.highPriority = highPriority;
    }

    public Set<Integer> getLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(Set<Integer> lowPriority) {
        this.lowPriority = lowPriority;
    }

    public Set<Integer> getNormalPriority() {
        return normalPriority;
    }

    public void setNormalPriority(Set<Integer> normalPriority) {
        this.normalPriority = normalPriority;
    }
}
