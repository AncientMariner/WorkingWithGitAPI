package org.xander.commitID.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CommitProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitProperties.class);

    private final String describe;
    private final String describeShort;
    private final String fullMessage;
    private final String id;
    private final String idAbbrev;
    private final String shortMessage;
    private final String time;
    private final String userEmail;
    private final String userName;

    @Autowired
    public CommitProperties(@Value("${git.commit.id.describe}") String describe,
                            @Value("${git.commit.id.describe-short}") String describeShort,
                            @Value("${git.commit.message.full}") String fullMessage,
                            @Value("${git.commit.id}") String id,
                            @Value("${git.commit.id.abbrev}") String idAbbrev,
                            @Value("${git.commit.message.short}") String shortMessage,
                            @Value("${git.commit.time}") String time,
                            @Value("${git.commit.user.email}") String userEmail,
                            @Value("${git.commit.user.name}") String userName) {
        this.describe = describe;
        this.describeShort = describeShort;
        this.fullMessage = fullMessage;
        this.id = id;
        this.idAbbrev = idAbbrev;
        this.shortMessage = shortMessage;
        this.time = time;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getDescribe() {
        return describe;
    }

    public String getDescribeShort() {
        return describeShort;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public String getId() {
        return id;
    }

    public String getIdAbbrev() {
        return idAbbrev;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getTime() {
        return time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("describe", this.describe)
                .append("describeShort", this.describeShort)
                .append("fullMessage", this.fullMessage)
                .append("id", this.id)
                .append("idAbbrev", this.idAbbrev)
                .append("shortMessage", this.shortMessage)
                .append("time", this.time)
                .append("userEmail", this.userEmail)
                .append("userName", this.userName)
                .toString();
    }

    @PostConstruct
    public void writeConfigurationToLog() {
        LOGGER.info("Starting application with the following commit properties: {}", this);
    }
}