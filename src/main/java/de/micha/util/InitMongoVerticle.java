package de.micha.util;

import de.micha.rest.Glider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class InitMongoVerticle extends AbstractVerticle {

    // Convenience for run in  IDE
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(InitMongoVerticle.class.getName(), res -> {
            if (res.failed())
                System.out.println("failure deploying Verticle" + res.cause().getMessage());
        });

    }

    @Override
    public void start() throws Exception {

        JsonObject config = Vertx.currentContext().config();

        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", config.getString("mongo.uri", "mongodb://localhost:27017"))
                .put("db_name", config.getString("mongo_db", "test"));

        MongoClient mongoClient = MongoClient.createShared(vertx, mongoconfig);

        Glider glider = new Glider("Skywalk Tequila", 2333.99f);
        glider.setItemId("2");

        JsonObject json = new JsonObject(Json.encode(glider));
        mongoClient.insert("glider", json, id -> System.out.println("Inserted id: " + id.result()));
    }
}
