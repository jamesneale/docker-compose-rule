/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.generator;

import static com.palantir.docker.compose.generator.DockerComposeFileGenerator.NamedContainer.containerNamed;
import static com.palantir.docker.compose.generator.util.ContainerDefinitions.SIMPLE_DB;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DockerComposeFileGeneratorShould {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void generated_file_starts_with_version_two() throws Exception {
        DockerComposeFileGenerator dockerCompose = DockerComposeFileGenerator.of(
                containerNamed("db", SIMPLE_DB)
        );

        File dockerComposeFile = temporaryFolder.newFile();
        dockerCompose.writeFile(() -> dockerComposeFile);
        String contents = readFileToString(dockerComposeFile);

        assertThat(contents, startsWith("version: \"3\""));
    }

    @Test
    public void generated_file_contains_container_definitions() throws Exception {
        DockerComposeFileGenerator dockerCompose = DockerComposeFileGenerator.of(
                containerNamed("db", SIMPLE_DB)
        );

        File dockerComposeFile = temporaryFolder.newFile();
        dockerCompose.writeFile(() -> dockerComposeFile);
        String contents = readFileToString(dockerComposeFile);

        String expected = "services:\n"
                        + "  db:\n"
                        + "    image: \"test/simple-db\"";

        assertThat(contents, containsString(expected));
    }

    @Test
    public void generated_file_works_for_multiple_containers() throws Exception {
        DockerComposeFileGenerator dockerCompose = DockerComposeFileGenerator.of(
                containerNamed("db1", SIMPLE_DB),
                containerNamed("db2", SIMPLE_DB)
        );

        File dockerComposeFile = temporaryFolder.newFile();
        dockerCompose.writeFile(() -> dockerComposeFile);
        String contents = readFileToString(dockerComposeFile);

        String expectedContents =
                "services:\n"
                        + "  db1:\n"
                        + "    image: \"test/simple-db\"\n"
                        + "  db2:\n"
                        + "    image: \"test/simple-db\"\n";

        assertThat(contents, containsString(expectedContents));
    }

}
