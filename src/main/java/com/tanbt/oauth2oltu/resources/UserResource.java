package com.tanbt.oauth2oltu.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
public class UserResource {

    /**
     * http://localhost:8080/api/user
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser() {
        return "{\"result\":\"Hello web api\"}";
    }

}
