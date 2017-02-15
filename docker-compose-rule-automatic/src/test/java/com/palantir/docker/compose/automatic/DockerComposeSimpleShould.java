/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.automatic;

import static com.palantir.docker.compose.automatic.DockerComposeAutomatic.NamedContainer.containerNamed;
import static com.palantir.docker.compose.automatic.util.ContainerDefinitions.SIMPLE_DB;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DockerComposeSimpleShould {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void generate_a_file_for_a_single_container() throws Exception {
        DockerComposeAutomatic dockerComposeSimple = DockerComposeAutomatic.of(containerNamed("db", SIMPLE_DB));

        File tempFolder = temporaryFolder.newFolder();
        dockerComposeSimple.before(() -> tempFolder);

        File expectedFile = getDockerComposeFile(tempFolder);
        assertThat(expectedFile.exists(), is(true));
    }

    @Test
    public void generated_file_starts_with_version_two() throws Exception {
        DockerComposeAutomatic dockerCompose = DockerComposeAutomatic.of(
                containerNamed("db", SIMPLE_DB)
        );

        File tempFolder = temporaryFolder.newFolder();
        dockerCompose.before(() -> tempFolder);

        File expectedFile = getDockerComposeFile(tempFolder);
        String contents = readFileToString(expectedFile);

        assertThat(contents, startsWith("version: \"2\""));
    }

    @Test
    public void generated_file_contains_container_definitions() throws Exception {
        DockerComposeAutomatic dockerCompose = DockerComposeAutomatic.of(
                containerNamed("db", SIMPLE_DB)
        );

        File tempFolder = temporaryFolder.newFolder();
        dockerCompose.before(() -> tempFolder);

        File expectedFile = getDockerComposeFile(tempFolder);
        String contents = readFileToString(expectedFile);

        String expected = "containers:\n"
                        + "  db:\n"
                        + "    image: \"test/simple-db\"";

        assertThat(contents, containsString(expected));
    }

    @Test
    public void generated_file_works_for_multiple_containers() throws Exception {
        DockerComposeAutomatic dockerCompose = DockerComposeAutomatic.of(
                containerNamed("db1", SIMPLE_DB),
                containerNamed("db2", SIMPLE_DB)
        );

        File tempFolder = temporaryFolder.newFolder();
        dockerCompose.before(() -> tempFolder);

        File expectedFile = getDockerComposeFile(tempFolder);
        String contents = readFileToString(expectedFile);

        String expectedContents =
                "containers:\n"
                        + "  db1:\n"
                        + "    image: \"test/simple-db\"\n"
                        + "  db2:\n"
                        + "    image: \"test/simple-db\"\n";

        assertThat(contents, containsString(expectedContents));
    }

    private static File getDockerComposeFile(File folder) {
        return folder.toPath().resolve("docker-compose.yaml").toFile();
    }

}
