package sample;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class toolDAO {

    private static final String DB_NAME = "inventoryDB";
    private static final String COLLECTION_NAME = "tool";

    public static MongoDatabase getDB() throws UnknownHostException {
        MongoDatabase db = DBManager.getDB(DB_NAME);
        return db;
    }

    public static MongoCollection<Document> getCollection() throws UnknownHostException {
        MongoDatabase db = getDB();
        MongoCollection<Document> dbCollection = db.getCollection(COLLECTION_NAME);
        return dbCollection;
    }

    public static Tool getToolFromDocument(Document doc) {
        String brandName = String.valueOf(doc.get("Brand Name"));
        String type = String.valueOf(doc.get("Type"));
        String model = String.valueOf(doc.get("Model"));
        String desc = String.valueOf(doc.get("Description"));
        double costPrice = (Double)doc.get("Cost Price");
        double askingPrice = (Double)doc.get("Asking Price");
        boolean serviced = (Boolean)doc.get("Serviced");
        Tool tool = new Tool(doc.get("_id").toString(), brandName, type, model, desc, costPrice, askingPrice, serviced);
        return tool;
    }

    public static Tool getTool(Tool tool) throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Document filter = buildFullDocument(tool);
        Document result = dbCollection.find(filter).first();
        Tool t = getToolFromDocument(result);
        return t;
    }

    public static List<Tool> buildToolList(MongoCursor<Document> dbCursor) {
        List<Tool> tools = new ArrayList();
        try {
            while (dbCursor.hasNext()) {
                Document doc = dbCursor.next();
                Tool tool = getToolFromDocument(doc);
                tools.add(tool);
            }
        } finally {
            dbCursor.close();
        }
        return tools;
    }

    public static Document buildFullDocument(Tool tool) {
        Document doc = new Document("Brand Name", tool.getBrandName())
                .append("Type", tool.getType())
                .append("Model", tool.getModel())
                .append("Description", tool.getDesc())
                .append("Cost Price", tool.getCostPrice())
                .append("Asking Price", tool.getAskingPrice())
                .append("Serviced", tool.isServiced());

        return doc;
    }

    public static void saveTool(Tool tool) throws UnknownHostException {
        Document doc = buildFullDocument(tool);
        MongoCollection<Document> dbCollection = getCollection();
        dbCollection.insertOne(doc);
    }

    public static void saveTools(List<Tool> tools) throws UnknownHostException {
        List<Document> docs = new ArrayList<>();
        for (Tool tool : tools) {
            Document doc = buildFullDocument(tool);
            docs.add(doc);
        }
        MongoCollection<Document> dbCollection = getCollection();
        dbCollection.insertMany(docs);
    }

    public static void deleteTool(Tool tool) throws UnknownHostException {
        Document doc = buildFullDocument(tool);
        MongoCollection<Document> dbCollection = getCollection();
        dbCollection.deleteOne(doc);
    }

    public static void deleteAllTools() throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        MongoCursor<Document> cursor = dbCollection.find().iterator();
        while (cursor.hasNext()) {
            dbCollection.deleteOne(cursor.next());
        }
    }

    public static List<Tool> getAllTools() throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        MongoCursor<Document> dbCursor = dbCollection.find().iterator();
        List<Tool> tools = new ArrayList();

        tools = buildToolList(dbCursor);
        return tools;
    }

    // Textfields search

    public static <T> List<Tool> getToolsBySpec(String spec, T val) throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Document filter = new Document(spec, val);
        MongoCursor<Document> dbCursor = dbCollection.find(filter).iterator();
        List<Tool> tools = new ArrayList();

        tools = buildToolList(dbCursor);
        return tools;
    }

    public static void updateToolAddCustomer(Tool tool) throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Bson filter = eq("_id", new ObjectId(tool.getId()));
        Bson updateOperation = combine(set("Customer", tool.getCustomer()), set("Customer Phone Number", tool.getPhoneNumber()));
        dbCollection.updateOne(filter, updateOperation);
    }

    public static void updateToolAsSold(List<Tool> tools) throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Tool tool = tools.get(0);
        Bson filter = eq("_id", new ObjectId(tool.getId()));
        Bson updateOperation = combine(set("Sold", true), set("Sale Price", tool.getSalePrice()));
        dbCollection.updateOne(filter, updateOperation);
    }

    public static void updateToolsServicedStatus(List<Tool> tools) throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Bson filter = null;
        Bson updateOperation = null;
        for (Tool tool : tools) {
            filter = eq("_id", new ObjectId(tool.getId()));
            updateOperation = set("Serviced", tool.isServiced());
            dbCollection.updateOne(filter, updateOperation);
        }

    }

    // Button functions

    public static List<Tool> getUnservicedTools() throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Document filter = new Document("Serviced", false);
        MongoCursor<Document> dbCursor = dbCollection.find(filter).iterator();
        List<Tool> unservicedTools = new ArrayList();

        unservicedTools = buildToolList(dbCursor);
        return unservicedTools;
    }

    public static void setToolAsServiced(Tool tool) throws UnknownHostException {
        MongoCollection<Document> dbCollection = getCollection();
        Document queryDoc = new Document("_id", new ObjectId(tool.getId()));
        Document updateDoc = new Document("Serviced", true);

        dbCollection.updateOne(queryDoc, updateDoc);
    }

}
