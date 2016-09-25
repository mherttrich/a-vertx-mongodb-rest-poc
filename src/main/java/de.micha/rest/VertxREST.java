
package de.micha.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;
import java.util.stream.Collectors;


public class VertxREST extends AbstractVerticle {

    private MongoClient mongoClient;

    // Convenience for run in  IDE
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(VertxREST.class.getName(), res -> {
            if (res.failed())
                System.out.println("failure deploying Verticle" + res.cause().getMessage());
        });
    }

    @Override
    public void start() {
        setUpMongo();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/glider/:ID").handler(this::handleGet);
        router.get("/glider").handler(this::handleGetList);
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }


    private void handleGetList(RoutingContext routingContext) {
        mongoClient.find("glider", new JsonObject(), results -> {
            List<JsonObject> jsonObjects = results.result();

          /* is this non blocking?!
             mongoClient.find() returns immediately, but the REST client just
             gets results, after mongo delivered all results
          */
            List<Glider> glider = jsonObjects.stream()
                    .map(json -> Json.decodeValue(json.toString(), Glider.class))
                    .collect(Collectors.toList());
            // do something else with gliders

            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(glider));
        });
    }

    private void handleGet(RoutingContext routingContext) {
        String id = routingContext.request().getParam("ID");
        HttpServerResponse response = routingContext.response();

        JsonObject query = new JsonObject().put("itemId", id);
        //feels strange, I would expect a list, but JsonObject is backed by Map
        JsonObject fields = new JsonObject().put("name", 1).put("price", 2).put("itemId", 1);

        mongoClient.findOne("glider", query, fields, res -> {
            JsonObject jsonObject = res.result();
            if (jsonObject == null) {
                response.setStatusCode(404);
            } else {
                Glider glider = Json.decodeValue(jsonObject.toString(), Glider.class);
                // do something else with glider
                // other just pass through JsonObject

                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(glider));
            }
        });
    }

    private void setUpMongo() {
        JsonObject config = Vertx.currentContext().config();

        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", config.getString("mongo.uri", "mongodb://localhost:27017"))
                .put("db_name", config.getString("mongo_db", "test"));

        mongoClient = MongoClient.createShared(vertx, mongoconfig);
    }
}
