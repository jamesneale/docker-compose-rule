/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.simple;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.junit.Test;

public class DockerContainerDefinitionShould {

    @Test
    public void serialize_as_image_name_only() throws Exception {
        DockerContainerDefinition definition = DockerContainerDefinition.builder()
                .image("kiasaki/alpine-postgres")
                .build();

        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.setSerializationInclusion(NON_ABSENT);

        String composeFileString = mapper.writeValueAsString(definition);

        assertThat(composeFileString, is("image: \"kiasaki/alpine-postgres\"\n"));
    }
}
