package com.dajudge.kilodemo;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;
import static java.util.stream.Collectors.toMap;

public class K3sTest {
    @Test
    public void test() {
        var agent = createAgent();
        agent.start();

        System.out.println("BLOCK HERE");

        agent.stop();
    }

    private static K3sAgentContainer<?> createAgent() {
        final Map<String, String> config = loadConfig("demo.config");
        K3sAgentContainer<?> agent = new K3sAgentContainer<>(config.get("HOST"), config.get("TOKEN"))
                .withExposedPorts(30001)
                .waitingFor(new WaitAllStrategy())
                .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(K3sTest.class)));
        return agent;
    }

    @NotNull
    private static Map<String, String> loadConfig(final String configFileName) {
        return Stream.of(readConfigFile(configFileName).split("\n"))
                .filter(line -> !line.trim().isEmpty())
                .map(line -> line.split("=", 2))
                .collect(toMap(it -> it[0], it -> it[1]));
    }

    @NotNull
    private static String readConfigFile(final String config) {
        try {
            return readString(new File(config).toPath());
        } catch (final IOException e) {
            throw new AssertionError("Failed to read config: %s".formatted(config), e);
        }
    }
}
