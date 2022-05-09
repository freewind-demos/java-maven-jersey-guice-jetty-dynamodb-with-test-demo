package example;

import com.google.inject.Injector;
import example.db.UsersDb;
import example.models.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.grizzly.http.server.HttpServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HelloResourceTest {

    private static HttpServer server;
    private static WebTarget target;

    private UsersDb userDynamoDb;

    @BeforeAll
    public static void beforeAllTests() throws Exception {
        System.out.println("### > beforeAllTests");
        System.out.flush();
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        Injector injector = Main.createInjector();
        this.userDynamoDb = injector.getInstance(UsersDb.class);
        this.userDynamoDb.recreate();
    }

    @AfterAll
    public static void afterAllTests() {
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Test
    public void testCreateUser() {
        String response = target.path("/users").request().post(Entity.json("""
            {"username":"aaa", "email": "test@test.com", "age": 111}
                        """), String.class);
        assertThat(new JSONObject(response)).usingRecursiveComparison().isEqualTo(new JSONObject("""
            {"id":1,"email":"test@test.com","age":111,"username":"aaa"}
            """));
    }

    @Test
    public void testListAll() throws JSONException {
        this.userDynamoDb.insertItem(User.build("aaa", "test@test.com", 222));

        String response = target.path("/users")
            .request(MediaType.APPLICATION_JSON)
            .get(String.class);

        // convert json string to JSONArray
        JSONArray actual = new JSONArray(response);

        String expected = """
             [{"id":1,"email":"test@test.com","age":222,"username":"aaa"}]
            """;
        assertThat(actual).usingRecursiveComparison().isEqualTo(new JSONArray(expected));
    }
}
