package example.resources;

import example.models.User;
import example.db.UsersDb;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.inject.Inject;
import java.util.List;

@Path("/users")
public class HelloResource {

    private UsersDb userDb;

    @Inject
    public HelloResource(UsersDb userDb) {
        this.userDb = userDb;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public User createUser(User user) {
        System.out.println("### createUser:" + user);
        try {
            userDb.insertItem(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        try {
            return userDb.getItems();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
