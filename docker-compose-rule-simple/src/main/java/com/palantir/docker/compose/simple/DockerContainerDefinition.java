/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableDockerContainerDefinition.class)
@JsonSerialize(as = ImmutableDockerContainerDefinition.class)
public abstract class DockerContainerDefinition {

    public abstract String image();

    public static ImmutableDockerContainerDefinition.Builder builder() {
        return ImmutableDockerContainerDefinition.builder();
    }
}
