package ru.terra.server;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.http.server.util.MimeType;
import ru.terra.server.config.Config;
import ru.terra.server.constants.ConfigConstants;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Date: 20.05.14
 * Time: 19:10
 */
public class ServerBoot {

    protected static HttpServer startServer() throws IOException {
        Config config = Config.getConfig();
        String url = "http://" + config.getValue(ConfigConstants.SERVER_ADDR, ConfigConstants.SERVER_ADDR_DEFAULT);
        URI uri = UriBuilder.fromUri(url).port(Integer.parseInt(config.getValue(ConfigConstants.SERVER_PORT, ConfigConstants.SERVER_PORT_DEFAULT))).build();
        final HttpServer webserver = GrizzlyServerFactory.createHttpServer(uri);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                webserver.stop();
            }
        });
        webserver.getServerConfiguration().addHttpHandler(
                new StaticHttpHandler(Config.getConfig().getValue(ConfigConstants.SERVER_STATIC_RESOURCES, ConfigConstants.SERVER_STATIC_RESOURCES_DEFAULT)) {
                    @Override
                    public void start() {
                        super.start();
                        System.out.println("Starting static resources handler");
                    }

                    @Override
                    protected boolean handle(String uri, Request request, Response response) throws Exception {
                        System.out.println("Handling uri " + uri);
                        boolean found = false;

                        final File[] fileFolders = docRoots.getArray();
                        if (fileFolders == null) {
                            return false;
                        }

                        File resource = null;

                        for (int i = 0; i < fileFolders.length; i++) {
                            final File webDir = fileFolders[i];
                            // local file
                            resource = new File(webDir, uri);
                            final boolean exists = resource.exists();
                            final boolean isDirectory = resource.isDirectory();

                            if (exists && isDirectory) {
                                final File f = new File(resource, "/index.html");
                                if (f.exists()) {
                                    resource = f;
                                    found = true;
                                    break;
                                }
                            }

                            if (isDirectory || !exists) {
                                found = false;
                            } else {
                                found = true;
                                break;
                            }

                            if (exists && !isDirectory)
                                found = true;
                        }

                        if (!found) {
                            Logger.getLogger(this.getClass()).warn("File not found: " + resource);
                            return false;
                        }

                        pickupContentType(response, resource);
                        addToFileCache(request, response, resource);
                        sendFile(response, resource);
                        return true;
                    }

                    private void pickupContentType(final Response response,
                                                   final File file) {
                        if (!response.getResponse().isContentTypeSet()) {
                            final String path = file.getPath();
                            String substr;
                            int dot = path.lastIndexOf('.');
                            if (dot < 0) {
                                substr = file.toString();
                                dot = substr.lastIndexOf('.');
                            } else {
                                substr = path;
                            }

                            if (dot > 0) {
                                String ext = substr.substring(dot + 1);
                                String ct = MimeType.get(ext);
                                if (ct != null) {
                                    response.setContentType(ct);
                                }
                            } else {
                                response.setContentType(MimeType.get("html"));
                            }
                        }
                    }
                }, "/resources");
        return webserver;
    }


    public void start() throws IOException {
        HttpServer httpServer = startServer();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
