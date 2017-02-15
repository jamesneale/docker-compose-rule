/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple.util;

import com.palantir.docker.compose.simple.DockerContainerDefinition;

public class ContainerDefinitions {

    private ContainerDefinitions() {
        // Utility class
    }

    public static final DockerContainerDefinition SIMPLE_DB = DockerContainerDefinition.builder()
            .image("test/simple-db")
            .build();

}
