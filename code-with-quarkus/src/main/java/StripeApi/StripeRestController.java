package StripeApi;

import Model.StripeModel;
import Model.Users;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.ProductCollection;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Path("/Stripe")
@Slf4j
public class StripeRestController {
    @Inject
    Payments payments;

    ArrayList<StripeModel> Creation = new ArrayList<>();
    @Path("/Customer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Customer CreateCustomer(Users details){
       Customer customer = payments.AddCustomer(details);
        return customer;
    }
    @Path("/Customer/{limit}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerCollection GetAllCutomer(@PathParam("limit")String limit){
        log.info("MY RESPONSE"+payments.GetAllCustomers(limit));
        CustomerCollection collection = payments.GetAllCustomers(limit);

        return collection;
    }

    @Path("/Customer/search/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerSearchResult SearchforCutomer(@PathParam("email")String email){
        log.info("MY RESPONSE"+payments.SeachCustomer(email));
        return payments.SeachCustomer(email);

    }
    @Path("/Products/{count}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProductCollection listProduct(@PathParam("count") int count){
        log.info("Products"+payments.GetAllProducts(count));
       return payments.GetAllProducts(count);
    }

    @Path("/Customer/checkout/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckoutSession(StripeModel model){
        Creation.add(model);
       log.info("EMAIL ADV"+ model.getEmail());
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(payments.CheckoutSession(model).getId());

        return jsonResponse;
  }

}
