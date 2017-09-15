package com.tanbt.oauth2oltu.resources;

import org.glassfish.jersey.server.ResourceConfig;

public class MyResource extends ResourceConfig {

    public MyResource() {
        register(UserResource.class);
    }

}
