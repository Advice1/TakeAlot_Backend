package subscription;

import Database.ReactiveMongoDB;
import Database.ReactiveMongoDbProducts;
import Model.Products;
import Model.Users;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/subscription")
@Slf4j
public class SubscriptionRestController {
    @Inject
    ReactiveMongoDB MongoDB;

    @Inject
    ReactiveMongoDbProducts MongoDbProducts;
    @Inject
    GreetingService service;

 ArrayList<Users> users = new ArrayList<>();
 ArrayList<Products> cart = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList RetrievingUserDetails(){
        return users;
    }

    //todo:OAuth, JWT (JSON Web Tokens), or a custom authentication system that uses HTTPS to securely transmit credentials
    @Path("{username}/{password}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public boolean UserLogin(@PathParam("username") String username,String password){
        boolean results= users.stream().anyMatch(items -> items.getEmail().equals(username) && items.getPassword().equals(password));
        log.info("login"+results);
        return results;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Users addingUsersDetails(Users store){
        users.add(store);
        MongoDB.addUser(store).subscribe().with((data) -> {
                log.info("User added successfully.");
            },
            failure -> {
                log.error("Failed to add user: " + failure);
            }
        );
        return store;
    }

   /* @Path("/database/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Users> GetUsersDetails(@PathParam("email") String email){
     return MongoDB.FindbyEmail(email).onFailure().recoverWithItem(error ->{
         log.error("Eroor occured"+error);
         return (Users) Uni.createFrom().nothing();
     });
    }*/
   /* @Path("/database/Verify/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Boolean> CheckEmail(@PathParam("email") String email){
        return MongoDB.existEmail(email);
    }*/
    @Path("/cart/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  Products storeToCart(Products product){
        cart.add(product);

        MongoDbProducts.addTocart(product).subscribe().with(results ->{
            log.info("Success"+results);
        }, failure ->{
            log.error("fail"+failure);
        });

        return product;
    }

    @Path("/cart/exist/{username}/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean CheckProductExist(@PathParam("username") String username,String id){
        boolean anyMatchResult = cart.stream()
                .anyMatch(item -> item.getUser().equals(username)  && item.getId().equals(id));

        MongoDbProducts.checkproduct(username,id).subscribe().with(results->{
            log.info("item"+results);
        }, System.out::println);

        return anyMatchResult;
    }
    /*@Path("/cart/exist/database/{username}/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Boolean> CheckProductExistDatabase(@PathParam("username") String username,String id){
      return MongoDbProducts.checkproduct(username,id);
    }*/
    @Path("/cart/all/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Products> getAllCart(@PathParam("username") String username){

        List<Products> matchingElements = cart.stream()
                .filter(item ->  item.getUser().equals(username))
                .collect(Collectors.toList());


        return matchingElements;
    }
   /* @Path("/cart/all/database/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Products> getAllCartDatabase(@PathParam("username") String username){

       return MongoDbProducts.FindProductbyEmail(username);

    }*/
    @Path("cart/remove/{username}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteAllCart(@PathParam("username") String username){
        cart.removeIf(data ->data.getUser() == username); //todo:add condition need to add another condition
    }

    @Path("/cart/count/{username}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int getCartCount(@PathParam("username") String username){
       return  getAllCart(username).size();
        //return cart.size();
    }
    //todo:remove from cart
    @Path("/remove/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteCart(@PathParam("id") int id){
        cart.removeIf(data ->Integer.parseInt(data.getId())  == id); //todo:add condition need to add another condition
    }
    //TODO:Query param issue





}
