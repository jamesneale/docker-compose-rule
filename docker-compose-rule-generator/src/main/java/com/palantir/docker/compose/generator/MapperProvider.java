/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */
package com.palantir.docker.compose.generator;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

public class MapperProvider {

    private MapperProvider() {
        // should not be instantiated.
    }

    public static ObjectMapper mapper() {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.setSerializationInclusion(NON_ABSENT);
        return mapper;
    }
}
