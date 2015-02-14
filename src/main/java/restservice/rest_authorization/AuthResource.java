package restservice.rest_authorization;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import restservice.model.User;

/**
 * REST Web Service
 *
 * @author AMore
 */
@Stateless
@Path("auth")
public class AuthResource {

    @Context
    private UriInfo context;

    @PersistenceContext(unitName = "WebServices_PU")
    private EntityManager em;

    public AuthResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login/{username}/{password}")
    public Response getAuth(@PathParam("username") String username, @PathParam("password") String password) {

        User user = em.find(User.class, username);

        String authorized = String.valueOf(user != null && user.getPassword().equalsIgnoreCase(password));

        JsonObject value = Json.createObjectBuilder().add("authorized", authorized).build();

        return Response.status(200).entity(value.toString()).build();
    }

    @POST
    @Path("/createuser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        JsonObject value;

        // Check if the user exist already
        User addThisUser = em.find(User.class, user.getUsername());

        if (addThisUser == null) {

            em.persist(user);

            value = Json.createObjectBuilder()
                    .add("added", "true")
                    .add("username", user.getUsername())
                    .add("password", user.getPassword())
                    .build();
        } else {
            value = Json.createObjectBuilder()
                    .add("added", "false")
                    .build();
        }

        return Response.status(200).entity(value).build();
    }
    
    
    

    @PUT
    @Path("/updateuser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAuth(User user) {
        JsonObject value;

        // Get the existing user
        User updateThisUser = em.find(User.class, user.getUsername());

        if (updateThisUser != null) {

            updateThisUser.setPassword(user.getPassword());

            em.merge(updateThisUser);

            value = Json.createObjectBuilder()
                    .add("updated", "true")
                    .add("username", user.getUsername())
                    .add("password", user.getPassword())
                    .build();
        } else {
            value = Json.createObjectBuilder()
                    .add("updated", "false")
                    .add("reason", "User dont exist")
                    .build();
        }

        return Response.status(200).entity(value).build();
    }

    @DELETE
    @Path("/deleteuser/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAuth(@PathParam("username") String username) {
        JsonObject value;

        // Get the existing user
        User removeThisUser = em.find(User.class, username);

        if (removeThisUser != null) {

            em.remove(removeThisUser);

            value = Json.createObjectBuilder()
                    .add("deleted", "true")
                    .add("username", username)
                    .build();
        } else {
            value = Json.createObjectBuilder()
                    .add("deleted", "false")
                    .add("reason", "User dont exist")
                    .build();
        }

        return Response.status(200).entity(value).build();
    }
    

}
