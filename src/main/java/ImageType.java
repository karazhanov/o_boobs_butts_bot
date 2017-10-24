/**
 * @author karazhanov on 24.10.17.
 */
public enum ImageType {
    BOOBS(CONST.BOOBS_COMMAND, CONST.OBOOBS_API, CONST.OBOOBS_MEDIA),
    BUTTS(CONST.BUTTS_COMMAND, CONST.OBUTTS_API, CONST.OBUTTS_MEDIA),
    UNKNOWN(null, null, null);

    private final String command;
    private final String api;
    private final String media;

    ImageType(String command, String api, String media) {
        this.command = command;
        this.api = api;
        this.media = media;
    }

    public final String getCommand() {
        return command;
    }

    public String getApi() {
        return api;
    }

    public String getMedia() {
        return media;
    }
}
