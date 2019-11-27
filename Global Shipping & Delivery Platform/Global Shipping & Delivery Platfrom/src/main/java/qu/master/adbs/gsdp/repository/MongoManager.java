package qu.master.adbs.gsdp.repository;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.time.LocalDate;
import java.time.ZoneId;

public class MongoManager {
	
	private static MongoClient mongoClient;
	private final static String databaseName = "GSDP";
	private final static String databaseUrl = "localhost:27017";
	private final static String countersCollection = "counters";
	public static MongoClient getClient() {
		if (mongoClient == null) {
			mongoClient = new MongoClient(databaseUrl);
		}
		
		return mongoClient;
	}
	
	public static MongoDatabase getDatabase() {
		return getClient().getDatabase(databaseName);
	}
	
	public static MongoCollection<Document> getCollection(String collectionName) {
		return getDatabase().getCollection(collectionName);
	}
	
	public static int getNewId(String documentName) {
		int newId = 0;
		var collection = getCollection(countersCollection);
		var document = collection.findOneAndUpdate(eq("_id", documentName), inc("counter", 1));
		if (document == null) {
			document = new Document("_id", documentName).append("counter", 1);
			newId = 1;
			collection.insertOne(document);
		}
		
		else {
			newId = document.getInteger("counter") + 1;
		}
		
		return newId;
	}
	
	public static LocalDate getLocalDate(Document document, String key) {
		return document.getDate(key).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
