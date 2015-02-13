package ru.terra.server.constants;


public interface ConfigConstants {
    public static final String SESSION_TTL = "session.ttl";
    public static final String SESSION_TTL_DEFAULT = "600";
    public static final String SERVER_ADDR = "server.addr";
    public static final String SERVER_ADDR_DEFAULT = "0.0.0.0/terramarket/";
    public static final String PESISTANCE_UNIT = "server.pu";
    public static final String PESISTANCE_UNIT_DEFAULT = "tmpu";
    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_PORT_DEFAULT = "8080";
    public static final String SERVER_STATIC_RESOURCES = "server.static_resources.path";
    public static final String SERVER_STATIC_RESOURCES_DEFAULT = "resources/";
    public static final String SERVER_SERVE_STATIC = "server.static_resources.enable";
    public static final String SERVER_PACKAGE = "server.package";
    public static final String SERVER_REST = "server.rest";
}
