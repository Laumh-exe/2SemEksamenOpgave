package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class PageController {


    public static void goToLoginPage(Context ctx) {

        String currentpage = ctx.formParam("currentpage");
        ctx.sessionAttribute("currentpage", currentpage);

        ctx.redirect("/login");

    }
}
