/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.generator;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;

public class DockerComposeGenerator {
    private static final String DOCKER_COMPOSE_HEADER = "version: \"2\"";

    private final DockerComposeDefinition definition;

    public DockerComposeGenerator(DockerComposeDefinition definition) {
        this.definition = definition;
    }

    public void before(Supplier<File> temporaryFolderSupplier) {
        File temporaryFolder = temporaryFolderSupplier.get();

        try {
            File dockerComposeFile = temporaryFolder.toPath().resolve("docker-compose.yaml").toFile();
            if (!dockerComposeFile.createNewFile()) {
                throw new IOException("Unable to create docker-compose file!");
            }
            FileUtils.write(dockerComposeFile, dockerComposeFileContents());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String dockerComposeFileContents() {
        return String.join("\n", DOCKER_COMPOSE_HEADER, definition.toString());
    }

    public static DockerComposeGenerator of(NamedContainer... containers) {
        ImmutableDockerComposeDefinition.Builder builder = DockerComposeDefinition.builder();
        Arrays.stream(containers).forEach(container -> builder.putContainers(container.name, container.definition));
        return new DockerComposeGenerator(builder.build());
    }

    public static class NamedContainer {
        private final String name;
        private final DockerContainerDefinition definition;

        public NamedContainer(String name, DockerContainerDefinition definition) {
            this.name = name;
            this.definition = definition;
        }

        public static NamedContainer containerNamed(String name, DockerContainerDefinition definition) {
            return new NamedContainer(name, definition);
        }
    }
}
