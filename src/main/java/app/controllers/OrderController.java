package app.controllers;


import io.javalin.Javalin;

import java.sql.Connection;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionpool){
        app.post("addtobasket", ctx)
    }

}
