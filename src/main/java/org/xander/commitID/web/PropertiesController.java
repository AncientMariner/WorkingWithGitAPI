package org.xander.commitID.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xander.commitID.config.ApplicationProperties;
import org.xander.commitID.config.BuildProperties;
import org.xander.commitID.config.CommitProperties;
import org.xander.commitID.config.GitProperties;

@RestController
final class PropertiesController {

    private final ApplicationProperties applicationProperties;
    private final GitProperties gitProperties;
    private final CommitProperties commitProperties;
    private final BuildProperties buildProperties;

    @Autowired
    PropertiesController(ApplicationProperties applicationProperties,
                         GitProperties gitProperties,
                         CommitProperties commitProperties,
                         BuildProperties buildProperties) {
        this.applicationProperties = applicationProperties;
        this.gitProperties = gitProperties;
        this.commitProperties = commitProperties;
        this.buildProperties = buildProperties;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getDefault(@Value("${app.server.protocol}") String protocol,
                      @Value("${app.server.host}") String serverHost,
                      @Value("${app.server.port}") int serverPort) {
        return "There are different configurations:<br /><br />" +
                "Application Configuration - <a href=\"" + protocol + "://" + serverHost + ":" + serverPort + "/app_config" + "\">Application configuration</a>" + "<br />" +
                "Build Configuration - <a href=\"" + protocol + "://" + serverHost + ":" + serverPort + "/build_config" + "\">Build configuration</a>" + "<br />" +
                "Commit Configuration - <a href=\"" + protocol + "://" + serverHost + ":" + serverPort + "/commit_config" + "\">Commit configuration</a>" + "<br />" +
                "Git Configuration - <a href=\"" + protocol + "://" + serverHost + ":" + serverPort + "/git_config" + "\">Git configuration</a>";
    }

    @RequestMapping(value = "/app_config", method = RequestMethod.GET)
    ApplicationProperties getAppConfiguration() {
        return applicationProperties;
    }

    @RequestMapping(value = "/build_config", method = RequestMethod.GET)
    BuildProperties getBuildConfiguration() {
        return buildProperties;
    }

    @RequestMapping(value = "/commit_config", method = RequestMethod.GET)
    CommitProperties getCommitConfiguration() {
        return commitProperties;
    }

    @RequestMapping(value = "/git_config", method = RequestMethod.GET)
    GitProperties getGitConfiguration() {
        return gitProperties;
    }
}