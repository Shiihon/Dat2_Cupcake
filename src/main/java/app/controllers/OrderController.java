package app.controllers;
import app.entities.CupcakePart;
import app.entities.OrderItem;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("addtocart", ctx -> addToCart(ctx, connectionPool));
        app.post("myorders", ctx -> viewMyOrders(ctx, connectionPool));
        app.get("viewcart", ctx -> viewMyCart(ctx, connectionPool));
        app.post("ordernow", ctx -> placeOrder());
        app.post("cancelorderinoverview", ctx -> cancelOrderInOverview());
        app.post("orderisready", ctx -> orderReadyToPickup());
        app.post("rejectorder", ctx -> rejectOrder());
        app.get("backtoordersite", ctx -> ctx.render("user-frontpage.html"));
        app.post("cancelorder", ctx -> cancelOrder());

        //loader liste af bunde og topp når user-frontpage bliver loaded
        app.get("/user-frontpage", ctx -> {
            List<CupcakePart> bottoms = CupcakeMapper.getCupcakeBottoms(connectionPool);
            System.out.println("Bottoms: " + bottoms);
            List<CupcakePart> tops = CupcakeMapper.getCupcakeTops(connectionPool);
            ctx.attribute("bottoms", bottoms);
            ctx.attribute("tops", tops);
            ctx.render("user-frontpage.html");
        });
    }

    public static void addToCart(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            int bottomId = Integer.parseInt(ctx.formParam("bottom"));
            int topId = Integer.parseInt(ctx.formParam("tops"));
            int amount = Integer.parseInt(ctx.formParam("amount"));

            CupcakePart bottomPart = CupcakeMapper.getCupcakePartById(bottomId, connectionPool, CupcakePart.Type.BOTTOM);
            CupcakePart topPart = CupcakeMapper.getCupcakePartById(topId, connectionPool, CupcakePart.Type.TOP);

            int itemPrice = bottomPart.getPrice() + topPart.getPrice();
            int totalPrice = itemPrice * amount;

            OrderItem newItem = new OrderItem(-1, topPart, bottomPart, amount, totalPrice);

            List<OrderItem> basket = ctx.sessionAttribute("basket");
            if (basket == null) {
                basket = new ArrayList<>();
            }

            basket.add(newItem);
            System.out.println(newItem);

            ctx.sessionAttribute("basket", basket);
            ctx.redirect("/user-frontpage");

        } catch (NumberFormatException | DatabaseException e) {
            ctx.attribute("Error adding to cart: " + e.getMessage());
            ctx.render("user-frontpage.html");
        }
    }

    private static void viewMyOrders(Context ctx, ConnectionPool connectionPool) {
        ctx.render("my-orders.html");
    }

    private static void viewMyCart(Context ctx, ConnectionPool connectionPool) {
        List<OrderItem> basket = ctx.sessionAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        ctx.attribute("basket", basket);
        ctx.render("order-overview.html");
    }

    private static void placeOrder() {
    }

    private static void cancelOrderInOverview() {
    }

    private static void orderReadyToPickup() {
    }

    private static void rejectOrder() {
    }

    private static void cancelOrder() {
    }

}
