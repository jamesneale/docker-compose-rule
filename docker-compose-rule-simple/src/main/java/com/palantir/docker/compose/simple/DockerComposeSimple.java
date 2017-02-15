/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;

public class DockerComposeSimple {
    private final DockerComposeDefinition definition;

    public DockerComposeSimple(DockerComposeDefinition definition) {
        this.definition = definition;
    }

    public void before(Supplier<File> temporaryFolderSupplier) {
        File temporaryFolder = temporaryFolderSupplier.get();

        try {
            File dockerComposeFile = temporaryFolder.toPath().resolve("docker-compose.yaml").toFile();

            if (!dockerComposeFile.createNewFile()) {
                throw new IOException("Unable to create docker-compose file!");
            }

            FileUtils.write(dockerComposeFile, "version: \"2\"");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static DockerComposeSimple of(NamedContainer... containers) {
        ImmutableDockerComposeDefinition.Builder builder = DockerComposeDefinition.builder();
        Arrays.stream(containers).forEach(container -> builder.putContainers(container.name, container.definition));
        return new DockerComposeSimple(builder.build());
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
