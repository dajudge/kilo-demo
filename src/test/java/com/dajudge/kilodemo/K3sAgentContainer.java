package com.dajudge.kilodemo;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;

import static com.github.dockerjava.api.model.DockerObjectAccessor.overrideRawValue;

public class K3sAgentContainer<T extends K3sAgentContainer<T>> extends GenericContainer<T> {
    private static final HashMap<String, String> TMP_FILESYSTEMS = new HashMap<>() {{
        put("/run", "");
        put("/var/run", "");
    }};
    public K3sAgentContainer(final String server, final String token) {
        super(DockerImageName.parse("rancher/k3s:v1.25.0-k3s1"));
        this
                .withPrivilegedMode(true)
                .withCreateContainerCmdModifier(it -> overrideRawValue(it.getHostConfig(), "CgroupnsMode", "host"))
                .withFileSystemBind("/sys/fs/cgroup", "/sys/fs/cgroup", BindMode.READ_WRITE)
                .withTmpFs(TMP_FILESYSTEMS)
                .withCommand("agent", "--server", "https://%s:6443".formatted(server), "--token", token);
    }
}
