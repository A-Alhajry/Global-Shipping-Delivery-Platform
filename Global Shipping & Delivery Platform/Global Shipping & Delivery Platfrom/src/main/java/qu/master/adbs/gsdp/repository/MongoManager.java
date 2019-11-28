package qu.master.adbs.gsdp.repository;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MongoManager {
	
	private static MongoClient mongoClient;
	private final static String databaseName = "GSDP";
	private final static String databaseUrl = "localhost:27017";
	private final static String countersCollection = "counters";
	private final static String lookupsCollection = "lookups";

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
	
	public static void insertLookup(int id, String desc, String lookupName) {
		var mongoCollection = getCollection(lookupsCollection);
		var document = new Document();
		document.append("_id", id).append("description", desc).append("lookupname", lookupName);
		mongoCollection.insertOne(document);
	}
	
	public static List<MongoLookup> getLookups(String lookupName) {
		var mongoCollection = getCollection(lookupsCollection);
		List<MongoLookup> lookups = new ArrayList<>();
		var documents = mongoCollection.find(eq("lookupname", lookupName));
		if (documents != null) {
			try (var mongoCursor = documents.cursor()) {
				while (mongoCursor.hasNext()) {
					var document = mongoCursor.next();
					MongoLookup lookup = new MongoLookup(document.getInteger("_id"), document.getString("description"), document.getString("lookupname"));
					lookups.add(lookup);
				}
			}
		}
		
		return lookups;
	}
	
	public static LocalDate getLocalDate(Document document, String key) {
		return document.getDate(key).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static LocalDateTime getLocalDateTime(Document document, String key) {
		return document.getDate(key).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
