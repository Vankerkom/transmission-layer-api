package be.vankerkom.transmissionlayer.transmission;

public enum TorrentActionRequest {

    START("start"),
    START_NOW("start-now"),
    STOP("stop"),
    VERIFY("verify"),
    REANNOUNCE("reannounce");

    private final String methodName;

    TorrentActionRequest(final String methodName) {
        this.methodName = "torrent-" + methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
