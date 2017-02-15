/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import static com.palantir.docker.compose.simple.DockerComposeSimple.NamedContainer.containerNamed;
import static com.palantir.docker.compose.simple.util.ContainerDefinitions.SIMPLE_DB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DockerComposeSimpleShould {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void generate_a_file_for_a_single_container() throws Exception {
        DockerComposeSimple dockerComposeSimple = DockerComposeSimple.of(containerNamed("db", SIMPLE_DB));

        File tempFolder = temporaryFolder.newFolder();
        dockerComposeSimple.before(() -> tempFolder);

        File expectedFile = getDockerComposeFile(tempFolder);
        assertThat(expectedFile.exists(), is(true));
    }

    @Test
    public void generated_file_starts_with_version_two() throws Exception {
        DockerComposeSimple dockerCompose = DockerComposeSimple.of(
                containerNamed("db", SIMPLE_DB)
        );

        File tempFolder = temporaryFolder.newFolder();
        dockerCompose.before(() -> tempFolder);

        File expectedFile = getDockerComposeFile(tempFolder);
        String contents = FileUtils.readFileToString(expectedFile);

        assertThat(contents, startsWith("version: \"2\""));
    }

    private static File getDockerComposeFile(File folder) {
        return folder.toPath().resolve("docker-compose.yaml").toFile();
    }

}
