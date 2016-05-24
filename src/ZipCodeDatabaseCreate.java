import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ZipCodeDatabaseCreate {

    public static void main(String args[]) {

        String fileName = "./data/free-zipcode-database.csv";

        try {

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase db = mongoClient.getDatabase("zipCodes");
            MongoCollection<Document> documentMongoCollection = db.getCollection("zips");

            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

                Pattern p = Pattern.compile("^\"*([^\"^,]*)\"*,\"*([^\"^,]*)\"*,\"*([^\"^,]*)\"*,\"*([^\"^,]*)\"*,\"*([^\"^,]*)[^\n^\r]*$");
                stream.forEach(s -> {
                    Matcher m = p.matcher(s);
                    if (m.matches()) {
                        Document document = new Document();
                        document.append("zipcode", m.group(2));
                        document.append("city", m.group(4));
                        document.append("state", m.group(5));
                        documentMongoCollection.insertOne(document);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }

    }


}
