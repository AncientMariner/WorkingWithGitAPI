package org.xander.commitID.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GitProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitProperties.class);

    private String branch;
    private final BuildProperties build;
    private final CommitProperties commit;
    private final boolean dirty;
    private final String remoteOriginUrl;
    private final String tags;

    @Autowired
    public GitProperties(@Value("${git.branch}") String branch,
                         BuildProperties build,
                         CommitProperties commit,
                         @Value("${git.dirty}") boolean dirty,
                         @Value("${git.remote.origin.url}") String remoteOriginUrl,
                         @Value("${git.tags}") String tags) {
        this.branch = branch;
        this.build = build;
        this.commit = commit;
        this.dirty = dirty;
        this.remoteOriginUrl = remoteOriginUrl;
        this.tags = tags;
    }

    public String getBranch() {
        return branch;
    }

    public BuildProperties getBuild() {
        return build;
    }

    public CommitProperties getCommit() {
        return commit;
    }

    public boolean isDirty() {
        return dirty;
    }

    public String getRemoteOriginUrl() {
        return remoteOriginUrl;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("branch", this.branch)
                .append("build", this.build)
                .append("commit", this.commit)
                .append("dirty", this.dirty)
                .append("remoteOriginUrl", this.remoteOriginUrl)
                .append("tags", this.tags)
                .toString();
    }

    @PostConstruct
    public void writeGitCommitInformationToLog() {
        LOGGER.info("Starting application with the following Git commit: {}", this);
    }
}