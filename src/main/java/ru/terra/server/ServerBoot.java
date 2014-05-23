package ru.terra.server;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import org.apache.log4j.BasicConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import ru.terra.server.config.Config;
import ru.terra.server.constants.ConfigConstants;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Date: 20.05.14
 * Time: 19:10
 */
public class ServerBoot {

    protected static HttpServer startServer() throws IOException {
        String url = "http://" + Config.getConfig().getValue(ConfigConstants.SERVER_ADDR, ConfigConstants.SERVER_ADDR_DEFAULT);
        URI uri = UriBuilder.fromUri(url).port(Integer.parseInt(Config.getConfig().getValue(ConfigConstants.SERVER_PORT, ConfigConstants.SERVER_PORT_DEFAULT))).build();
        WebappContext context = new WebappContext("context");
        HttpServer webserver = GrizzlyServerFactory.createHttpServer(uri);
        context.deploy(webserver);
        return webserver;
    }


    public void start() throws IOException {
        BasicConfigurator.configure();
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
