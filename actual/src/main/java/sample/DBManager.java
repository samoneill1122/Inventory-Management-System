package sample;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;

public class DBManager {

    private static MongoDatabase db;

    public static MongoDatabase getDB (String name) throws UnknownHostException {
        String password = System.getenv("DBPSWD");
        System.out.println("password = " + password);
        MongoClient mongoClient = MongoClients.create("mongodb+srv://martin_oneill:" + password + "@cluster0.7ldqp.mongodb.net/inventoryDB?retryWrites=true&w=majority");
        System.out.println("TEST");
        if (db == null)
            db = mongoClient.getDatabase(name);
        return db;
    }

}
