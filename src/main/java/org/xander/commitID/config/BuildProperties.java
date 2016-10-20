package org.xander.commitID.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BuildProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildProperties.class);

    private final String time;
    private final String userEmail;
    private final String userName;

    @Autowired
    public BuildProperties(@Value("${git.build.time}") String time,
                           @Value("${git.build.user.email}") String userEmail,
                           @Value("${git.build.user.name}") String userName) {
        this.time = time;
        this.userEmail = userEmail;
        this.userName = userName;
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
                .append("time", this.time)
                .append("userEmail", this.userEmail)
                .append("userName", this.userName)
                .toString();
    }

    @PostConstruct
    public void writeConfigurationToLog() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting application with the following build properties: {}", this);
        }
    }
}