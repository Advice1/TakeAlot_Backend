package Database;

import Model.Products;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class ReactiveMongoDbProducts {
    @Inject
    ReactiveMongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    protected Testdata<Products> jacksonUtils = new Testdata<>( Products.class);

    private ReactiveMongoCollection getCollection(){
        return mongoClient.getDatabase(database).getCollection("Products");
    }
    public Uni<Products> addTocart(Products product){
        return getCollection().insertOne(jacksonUtils.documentFromObject(product));
    }
    public Uni<Boolean> checkproduct(String email, String id){
        // Build the query using the $and operator
        Document query = new Document("$and", Arrays.asList(
                new Document("user", email),
                new Document("id", id)
        ));

        return getCollection().countDocuments(query).map(count ->Integer.parseInt(count.toString())>0);
    }
    public Uni<Products> FindProductbyEmail(String email) {

        return getCollection().find(eq("user",email))
                .collect().asList();
    }
}
