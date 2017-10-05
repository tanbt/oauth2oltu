package com.tanbt.oauth2oltu.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tanbt.oauth2oltu.entity.Login;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.UserService;

@Path("user")
public class UserResource {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    /**
     * http://localhost:8080/api/user
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser() {
        return "{\"result\":\"Hello web api\"}";
    }

    /**
     *
     */
    @POST
    @Produces (MediaType.APPLICATION_JSON)
    @Path("/login")
    public String loginUser() {
        User user = new User();
        return "{\"result\":\"Hello web api\"}";
//        User user = userService.getUser("trungtanbui@gmail.com", "dfag");
//        return user;
        //return userService.getUser(data.getEmail(), data.getPassword());
    }

}
