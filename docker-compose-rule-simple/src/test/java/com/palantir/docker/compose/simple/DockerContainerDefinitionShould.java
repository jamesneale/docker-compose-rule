/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import static com.palantir.docker.compose.simple.MapperProvider.mapper;
import static com.palantir.docker.compose.simple.util.ContainerDefinitions.SIMPLE_DB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class DockerContainerDefinitionShould {

    @Test
    public void serialize_as_image_name_only() throws Exception {
        String composeFileString = mapper().writeValueAsString(SIMPLE_DB);
        assertThat(composeFileString, is("image: \"test/simple-db\"\n"));
    }
}
