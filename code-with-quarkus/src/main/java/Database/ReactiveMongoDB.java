package Database;

import Model.Products;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import Model.Users;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static com.mongodb.client.model.Filters.eq;


@ApplicationScoped
public class ReactiveMongoDB {

    //@Inject
   // MongoClient client;
     @Inject
     ReactiveMongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    protected Testdata<Products> jacksonUtils = new Testdata<>( Products.class);

    private ReactiveMongoCollection getCollection(){
        return mongoClient.getDatabase(database).getCollection("subscription");
    }
    public Uni<Boolean> existEmail(String email)
    {
        Document query= new Document("Email",email);
        return getCollection().countDocuments(query).map(count ->Integer.parseInt(count.toString())>0);
    }
    public Uni<Users> FindbyEmail(String email) {

        return getCollection().find(eq("Email",email))
               .collect().asList();
    }
    public Uni addUser(Users user){

   existEmail(user.getEmail()).subscribe().with(results -> {
    if (results) {
        System.out.println("results"+results);
    }
    else {
        System.out.println("false"+results);
    }
});

        Document document = new Document()
                .append("name", user.getName())
                .append("Email", user.getEmail())
                .append("Surname",user.getSurname())
                .append("Password",user.getPassword());

        return getCollection().insertOne(document).onItem().transformToUni(insertOneResult -> {
            if (insertOneResult != null) {
                System.out.println("lets check"+Uni.createFrom().voidItem()+"results");
                return Uni.createFrom().voidItem();
            } else {
                System.out.println("lets check"+Uni.createFrom().voidItem());
                return Uni.createFrom().failure(new RuntimeException("Insertion failed"));
            }
        });
    }


    public Uni<Users> FindbyID(Users user){
        return getCollection().find(new Document("Email", user.getEmail())).toUni();
    }



}
