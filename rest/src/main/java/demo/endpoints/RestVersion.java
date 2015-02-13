package demo.endpoints;

import demo.services.Version;

public class RestVersion {

    private final String version;
    private final String fullversion;
    private final String modifiedDate;
    private final String title;
    private final String buildNumber;
    private final String buildTag;
    private final String buildId;
    private final String gitHash;

    public RestVersion(final Version version) {
        this.version = version.getVersion();
        fullversion = version.getFullversion();
        modifiedDate = version.getModifiedDateAsString();
        title = version.getTitle();
        buildNumber = version.getBuildNumber();
        buildTag = version.getBuildTag();
        buildId = version.getBuildId();
        gitHash = version.getGitHash();
    }

    public String getVersion() {
        return version;
    }

    public String getFullversion() {
        return fullversion;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getBuildTag() {
        return buildTag;
    }

    public String getBuildId() {
        return buildId;
    }

    public String getGitHash() {
        return gitHash;
    }

}
