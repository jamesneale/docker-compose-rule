/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.generator;

import static com.palantir.docker.compose.generator.util.ContainerDefinitions.SIMPLE_DB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class DockerComposeDefinitionShould {

    @Test
    public void properly_assign_names_to_the_containers() throws Exception {
        DockerComposeDefinition dockerComposeDefinition = DockerComposeDefinition.builder()
                .putServices("db", SIMPLE_DB)
                .build();

        String expected = "services:\n"
                        + "  db:\n"
                        + "    image: \"test/simple-db\"";

        assertThat(dockerComposeDefinition.toString(), containsString(expected));
    }
}
