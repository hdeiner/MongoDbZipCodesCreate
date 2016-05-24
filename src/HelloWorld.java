import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloWorld {
    public static void main(String[] args) {

        try {

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase db = mongoClient.getDatabase("testdb");
            MongoCollection<Document> documentMongoCollection = db.getCollection("user");

            System.out.println("insert into mongo");
            Document document = new Document();
            document.append("firstName", "Howard");
            document.append("lastName", "Deiner");
            document.append("createdDate", new Date());
            documentMongoCollection.insertOne(document);

            System.out.println("query against mongo for all records");
            Document searchQuery1 = new Document();
            List<Document> documents1 = documentMongoCollection.find(searchQuery1).into(new ArrayList<Document>());
            for (Document d : documents1) {
                System.out.println(d);
            }

            System.out.println("query against mongo for Howard records");
            Document searchQuery2 = new Document();
            searchQuery2.put("firstName", "Howard");
            List<Document> documents = documentMongoCollection.find(searchQuery2).into(new ArrayList<Document>());
            for (Document d : documents) {
                System.out.println(d);
            }

            System.out.println("replicate one record in mongo, change an attribute, and write the new object back - verify with querying");
            Document query = new Document();
            query.put("firstName", "Howard");
            Document newDocument = new Document();
            newDocument.put("firstName", "Howard-lookatmeimupdated");
            Document updateObj = new Document();
            updateObj.put("$set", newDocument);
            documentMongoCollection.updateOne(query, updateObj);

            System.out.println("query against mongo for the changed document");
            Document searchQuery4 = new Document();
            searchQuery4.put("firstName", "Howard-lookatmeimupdated");
            List<Document> documents4 = documentMongoCollection.find(searchQuery4).into(new ArrayList<Document>());
            for (Document d : documents4) {
                System.out.println(d);
            }

        } catch (MongoException e) {
            e.printStackTrace();
        }

    }
}
