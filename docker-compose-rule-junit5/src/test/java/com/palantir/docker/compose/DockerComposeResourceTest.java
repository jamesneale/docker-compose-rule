/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.docker.compose;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DockerComposeResource.class)
public class DockerComposeResourceTest {

    @Test
    public void obtainComposeRule(DockerComposeRule dockerCompose) {
        assertSame(composeRule, dockerCompose);
    }

}
