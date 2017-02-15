/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableDockerComposeDefinition.class)
@JsonSerialize(as = ImmutableDockerComposeDefinition.class)
public abstract class DockerComposeDefinition {

    protected abstract Map<String, DockerContainerDefinition> containers();

    public static ImmutableDockerComposeDefinition.Builder builder() {
        return ImmutableDockerComposeDefinition.builder();
    }

    @Override
    public String toString() {
        try {
            return MapperProvider.mapper().writeValueAsString(this);
        } catch (Exception e) {
            return "Unable to generate string for object.";
        }
    }

}
