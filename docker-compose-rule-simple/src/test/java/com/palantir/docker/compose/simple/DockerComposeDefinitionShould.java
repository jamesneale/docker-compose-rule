/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import static com.palantir.docker.compose.simple.MapperProvider.mapper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class DockerComposeDefinitionShould {

    @Test
    public void properly_assign_names_to_the_containers() throws Exception {
        DockerContainerDefinition alpinePostgres = DockerContainerDefinition.builder()
                .image("test/alpine-postgres")
                .build();

        DockerComposeDefinition dockerComposeDefinition = DockerComposeDefinition.builder()
                .putContainers("db", alpinePostgres)
                .build();

        String dockerComposeString = mapper().writeValueAsString(dockerComposeDefinition);

        String expected = "containers:\n"
                        + "  db:\n"
                        + "    image: \"test/alpine-postgres\"";

        assertThat(dockerComposeString, containsString(expected));
    }
}
