package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("createAccount", ctx -> ctx.render("createAccount.html"));
        app.post("createAccount", ctx -> createAccount(ctx, connectionPool));
    }

    private static void createAccount(Context ctx, ConnectionPool connectionPool) {
        String userEmail = ctx.formParam("userEmail");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if (password1.equals(password2)) {
            try {
                UserMapper.createUser(userEmail, password1, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med E-mailen: " + userEmail +
                        ". Du kan nu logge på.");
                ctx.render("index.html");

            } catch (DatabaseException e) {
                ctx.attribute("message", "E-mailen findes allerede. Prøv igen, eller log ind");
                ctx.render("createuser.html");
            }
        } else {
            ctx.attribute("message", "Begge kodeord skal være ens! Prøv igen");
            ctx.render("createuser.html");
        }

    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        // Check om bruger findes i DB med de angivne email + password
        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            // Hvis ja, send videre til forsiden med login besked
            ctx.attribute("message", "Du er nu logget ind");
            ctx.render("user-frontpage.html");
        } catch (DatabaseException e) {
            // Hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }
}