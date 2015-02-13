package restservice.rest_authorization;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import restservice.model.User;
import restservice.model.UserStorage;

/**
 * REST Web Service
 *
 * @author AMore
 */
@Path("auth")
public class AuthResource {

    @Context
    private UriInfo context;

    private UserStorage usrStorage = UserStorage.getInstance();

    public AuthResource() {
    }

    /**
     * Example --> /auth/login/andre/word = True
     *
     * @param username
     * @param password
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login/{username}/{password}")
    public Response getAuth(@PathParam("username") String username, @PathParam("password") String password) {
        User foundUser = new User("null", "null");

        for (User usr : usrStorage.getUserStorage()) {
            if (usr.getUsername().equalsIgnoreCase(username)) {
                foundUser = usr;
            }
        }

        String authorized = String.valueOf(!foundUser.getUsername().equals("null") && foundUser.getPassword().equals(password));

        JsonObject value = Json.createObjectBuilder().add("authorized", authorized).build();

        return Response.status(200).entity(value.toString()).build();
    }

    @POST
    @Path("/createuser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        JsonObject value = null;

        System.out.println("before: " + usrStorage.getUserStorage().size());

        boolean createNewUser = false;

        for (User usr : usrStorage.getUserStorage()) {
            if (!usr.getUsername().equalsIgnoreCase(user.getUsername())) {
                System.out.println(usr.getUsername());
                createNewUser = true;
            }
        }

        if (createNewUser) {
            usrStorage.getUserStorage().add(user);

            value = Json.createObjectBuilder()
                    .add("AddedUser", Json.createObjectBuilder()
                            .add("username", user.getUsername())
                            .add("password", user.getPassword()))
                    .build();
        } else {
            value = Json.createObjectBuilder()
                    .add("AddedUser", Json.createObjectBuilder()
                            .add("UserAlreadyExist", "true"))
                    .build();
        }

        System.out.println("after: " + usrStorage.getUserStorage().size());

        return Response.status(200).entity(value).build();
    }

    /*
  
     @POST
     @Path("/send")
     @Consumes(MediaType.APPLICATION_JSON)
     public Response consumeJSON(String username) {
     return Response.status(200).entity(username).build();
     }
     */

    /*
     @PUT
     @Path("/{username}/{password}")
     @Produces("application/json")
     public String updateAuth(
     @PathParam("username") String username,
     @PathParam("password") String password) {

     userStorageMap.put(username, password);

     return "Updated password of user: " + username;

     }

     @DELETE
     @Path("/delete/{username}")
     @Produces("application/json")
     public String deleteAuth(
     @PathParam("username") String username,
     @PathParam("password") String password) {

     userStorageMap.remove(username);

     return "Deleted user: " + username;
     }

     */
}
