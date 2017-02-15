/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import static com.palantir.docker.compose.simple.MapperProvider.mapper;
import static com.palantir.docker.compose.simple.util.ContainerDefinitions.SIMPLE_DB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class DockerComposeDefinitionShould {

    @Test
    public void properly_assign_names_to_the_containers() throws Exception {
        DockerComposeDefinition dockerComposeDefinition = DockerComposeDefinition.builder()
                .putContainers("db", SIMPLE_DB)
                .build();

        String dockerComposeString = mapper().writeValueAsString(dockerComposeDefinition);

        String expected = "containers:\n"
                        + "  db:\n"
                        + "    image: \"test/simple-db\"";

        assertThat(dockerComposeString, containsString(expected));
    }
}
