package org.xander.commitID.config;

import com.nitorcreations.junit.runners.NestedRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(NestedRunner.class)
public class CommitPropertiesTest {

    public static class CreateNew {

        private final String DESCRIBE = "d8ffa16-dirty";
        private final String DESCRIBE_SHORT = "d8ffa16-dirty";
        private final String ID = "d8ffa168edf7a22d358a6829021a7a3b2fac6ae2";
        private final String ID_ABBREV = "d8ffa16";
        private final String MESSAGE_FULL = "LICENSE added";
        private final String MESSAGE_SHORT = "LICENSE added";
        private final String TIME = "10.05.2014 @ 21:01:11 EEST";
        private final String USER_EMAIL = "mail@mailServer.com";
        private final String USER_NAME = "name";

        private CommitProperties properties;

        @Before
        public void createCommitProperties() {
            properties = new CommitProperties(DESCRIBE,
                    DESCRIBE_SHORT,
                    MESSAGE_FULL,
                    ID,
                    ID_ABBREV,
                    MESSAGE_SHORT,
                    TIME,
                    USER_EMAIL,
                    USER_NAME);
        }

        @Test
        public void shouldSetDescribe() {
            assertThat(properties.getDescribe()).isEqualTo(DESCRIBE);
        }

        @Test
        public void shouldSetDescribeShort() {
            assertThat(properties.getDescribeShort()).isEqualTo(DESCRIBE_SHORT);
        }

        @Test
        public void shouldSetId() {
            assertThat(properties.getId()).isEqualTo(ID);
        }

        @Test
        public void shouldSetIdAbbrev() {
            assertThat(properties.getIdAbbrev()).isEqualTo(ID_ABBREV);
        }

        @Test
        public void shouldSetFullMessage() {
            assertThat(properties.getFullMessage()).isEqualTo(MESSAGE_FULL);
        }

        @Test
        public void shouldSetShortMessage() {
            assertThat(properties.getShortMessage()).isEqualTo(MESSAGE_SHORT);
        }

        @Test
        public void shouldSetTime() {
            assertThat(properties.getTime()).isEqualTo(TIME);
        }

        @Test
        public void shouldSetUserEmail() {
            assertThat(properties.getUserEmail()).isEqualTo(USER_EMAIL);
        }

        @Test
        public void shouldSetUserName() {
            assertThat(properties.getUserName()).isEqualTo(USER_NAME);
        }
    }
}