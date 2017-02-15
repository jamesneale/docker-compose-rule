/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.generator;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.ONE_HUNDRED_MILLISECONDS;
import static com.jayway.awaitility.Duration.ONE_MINUTE;
import static com.palantir.docker.compose.generator.DockerComposeFileGenerator.NamedContainer.containerNamed;
import static com.palantir.docker.compose.generator.util.ContainerDefinitions.ALPINE_POSTGRES;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import com.palantir.docker.compose.configuration.DockerComposeFiles;
import com.palantir.docker.compose.configuration.ProjectName;
import com.palantir.docker.compose.connection.ContainerName;
import com.palantir.docker.compose.connection.DockerMachine;
import com.palantir.docker.compose.execution.DefaultDockerCompose;
import com.palantir.docker.compose.execution.DockerCompose;
import java.io.File;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DockerComposeFileGeneratorIntegrationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void generated_file_with_single_container_brings_up_that_container() throws Exception {
        DockerComposeFileGenerator generator = DockerComposeFileGenerator.of(
                containerNamed("db", ALPINE_POSTGRES)
        );

        File dockerComposeFile = temporaryFolder.newFile();
        generator.writeFile(() -> dockerComposeFile);

        System.err.println(dockerComposeFile.getAbsolutePath());

        DockerCompose dockerCompose = new DefaultDockerCompose(
                DockerComposeFiles.from(dockerComposeFile.getAbsolutePath()),
                DockerMachine.localMachine().build(),
                ProjectName.random()
        );

        dockerCompose.pull();
        dockerCompose.up();

        List<String> runningContainers = dockerCompose
                .ps()
                .stream()
                .map(ContainerName::toString)
                .collect(toList());

        assertThat(runningContainers, contains("db"));

        // Ensure we don't leave containers running
        dockerCompose.down();
        dockerCompose.rm();
        await().pollInterval(ONE_HUNDRED_MILLISECONDS).atMost(ONE_MINUTE).until(dockerCompose.ps()::isEmpty);
    }
}
