package qu.master.adbs.gsdp.repository;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

import qu.master.adbs.gsdp.entity.Account;
import qu.master.adbs.gsdp.entity.Customer;
import qu.master.adbs.gsdp.entity.Employee;
import qu.master.adbs.gsdp.entity.Supplier;
import qu.master.adbs.gsdp.entity.UserCredentials;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@ApplicationScoped
//@Named("MongoAccountsRepository")
@RepositoryModeType(RepositoryMode.MONGO)
public class MongoAccountsRepository implements AccountsRepository {

	private final static String accountsCollection = "accounts";
	private final static String suppliersCollection = "suppliers";
	private final static String accountsDocument = "accounts_document";
	private final static String suppliersDocument = "suppliers_document";
	
	@Override
	public boolean authenticate(UserCredentials userCredentials) {
		var mongoCollection = MongoManager.getCollection(accountsCollection);
		BasicDBObject filter = new BasicDBObject();
		filter.append("email", userCredentials.getEmail()).append("password", userCredentials.getPassword());
		var document = mongoCollection.find(filter).first();
		return document != null;
	}

	@Override
	public int addCustomer(Customer customer) {
		int customerId = MongoManager.getNewId(accountsDocument);
		customer.setId(customerId);
		customer.setRole('C');
		var mongoCollection = MongoManager.getCollection(accountsCollection);
		mongoCollection.insertOne(this.getAccountDocument(customer));
		return customer.getId();
	}

	@Override
	public Customer getCustomer(int id) {
		var mongoCollection = MongoManager.getCollection(accountsCollection);
		var document = mongoCollection.find(eq("_id", id)).first();
		return this.getCustomerEntity(document);
	}

	@Override
	public List<Customer> getCustomers() {
		var mongoCollection = MongoManager.getCollection(accountsCollection);
		List<Customer> customers = new ArrayList<>();
		try (MongoCursor<Document> mongoCursor = mongoCollection.find().iterator()) {
			while(mongoCursor.hasNext()) {
				customers.add(this.getCustomerEntity(mongoCursor.next()));
			}
		}
		
		return customers;
	}

	@Override
	public int addEmployee(Employee employee) {
		var mongoCollection = MongoManager.getCollection(accountsCollection);
		int newId = MongoManager.getNewId(accountsDocument);
		employee.setId(newId);
		employee.setRole('E');
		mongoCollection.insertOne(this.getAccountDocument(employee));
		return newId;
	}

	@Override
	public int addSupplier(Supplier supplier) {
		var mongoCollection = MongoManager.getCollection(suppliersCollection);
		int newId = MongoManager.getNewId(suppliersDocument);
		supplier.setId(newId);
		mongoCollection.insertOne(this.getSupplierDocument(supplier));
		return newId;
	}

	@Override
	public List<Supplier> getSuppliers() {
		var mongoCollection = MongoManager.getCollection(suppliersCollection);
		List<Supplier> suppliers = new ArrayList<>();
		try(var mongoCursor = mongoCollection.find().iterator()) {
			while (mongoCursor.hasNext()) {
				suppliers.add(this.getSupplierEntity(mongoCursor.next()));
			}
		}
		
		return suppliers;
	}
	
	private Document getAccountDocument(Account account) {
		Document document = new Document();
		document.append("_id", account.getId());
		document.append("firstname", account.getFirstName());
		document.append("lastname", account.getLastName());
		document.append("email", account.getEmail());
		document.append("password", account.getPassword());
		document.append("phone", account.getPhone());
		document.append("birthdate", account.getBirthDate());
		return document;
	}
	
	private Document getSupplierDocument(Supplier supplier) {
		Document document = new Document();
		document.append("_id", supplier.getId());
		document.append("name", supplier.getName());
		document.append("description", supplier.getDescription());
		return document;
	}
	
	private Customer getCustomerEntity(Document document) {
		Customer customer = new Customer();
		customer.setId(document.getInteger("_id"));
		customer.setFirstName(document.getString("firstname"));
		customer.setLastName(document.getString("lastname"));
		customer.setEmail(document.getString("email"));
		customer.setPhone(document.getString("phone"));
		customer.setBirthDate(MongoManager.getLocalDate(document, "birthdate"));
		return customer;
	}
	
	private Supplier getSupplierEntity(Document document) {
		Supplier supplier = new Supplier();
		supplier.setId(document.getInteger("_id"));
		supplier.setName(document.getString("name"));
		supplier.setDescription(document.getString("description"));
		return supplier;
	}
	
	
	
}
