package example;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import example.db.UsersDb;
import example.resources.HelloResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {

    public static String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) throws Exception {
        startServer();
    }

    public static HttpServer startServer() throws Exception {
        startLocalDb();

        Injector injector = createInjector();
        injector.getInstance(UsersDb.class).createTable();

        return startApiServer(injector);
    }

    private static HttpServer startApiServer(Injector injector) {
        System.out.println("### > startApiServer");
        HelloResource resource = injector.getInstance(HelloResource.class);

        // scan packages
        final ResourceConfig config = new ResourceConfig();
        config.register(resource);

        return GrizzlyHttpServerFactory
            .createHttpServer(URI.create(BASE_URI), config);
    }

    public static Injector createInjector() {
        System.out.println("### > createInjector");
        return Guice.createInjector(new AbstractModule() {
            @Provides
            public AmazonDynamoDB provideAmazonDynamoDB() {
                AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:8000", Regions.US_EAST_1.getName());
                return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
            }
        });
    }

    private static void startLocalDb() throws Exception {
        System.out.println("### > startLocalDb");
        System.setProperty("sqlite4java.library.path", "native-libs");
        DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", String.valueOf(8000)});
        server.start();
    }


}