/*
 * Copyright 2016 Palantir Technologies, Inc. All rights reserved.
 */

package com.palantir.docker.compose;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.Iterables;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ContainerExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class DockerComposeResource implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    public static final String DOCKER_COMPOSE_RULE = "docker.compose.rule";

    @Override
    public void beforeAll(ContainerExtensionContext context) throws Exception {
        DockerComposeRule composeRule = getDockerComposeRule(context);
        context.getStore().put(DOCKER_COMPOSE_RULE, composeRule);
        composeRule.before();
    }

    private static DockerComposeRule getDockerComposeRule(ContainerExtensionContext context) {
        try {
            Class<?> testClass = context.getTestClass().get();

            List<Method> methods = Arrays.stream(testClass.getMethods())
                    .filter(method -> method.getReturnType() == DockerComposeRule.class)
                    .filter(method -> Modifier.isStatic(method.getModifiers()))
                    .collect(toList());

            return (DockerComposeRule) (Iterables.getOnlyElement(methods)).invoke(null);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("Test classes annotated with DockerComposeResource should"
                    + " provide a single DockerComposeRule object as a public final field.", e);
        }
    }

    @Override
    public void afterAll(ContainerExtensionContext context) throws Exception {
        context.getStore()
               .get(DOCKER_COMPOSE_RULE, DockerComposeRule.class)
               .after();
    }

    @Override
    public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == DockerComposeRule.class;
    }

    @Override
    public DockerComposeRule resolve(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return extensionContext.getStore().get(DOCKER_COMPOSE_RULE, DockerComposeRule.class);
    }
}
