package controllers;

import ninja.Result;
import ninja.AssetsController;

import ninja.jaxy.GET;
import ninja.jaxy.Path;
import ninja.jaxy.Order;

import com.google.inject.Singleton;
import com.google.inject.Inject;


/**
 * Static File Controller
 *
 */
@Singleton
@Path("/assets")
public class CustomAssetsController {

    @Inject
    AssetsController assetsController;

    @Path("/webjars/{fileName: .*}")
    @GET
    @Order(1)
    public Result serveWebJars() {
        return assetsController.serveWebJars();
    }

    @Path("/{fileName: .*}")
    @GET
    @Order(2)
    public Result serveStatic() {
        return assetsController.serveStatic();
    }
}