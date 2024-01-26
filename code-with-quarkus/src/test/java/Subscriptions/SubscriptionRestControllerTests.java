package Subscriptions;

import Model.Products;
import Model.Users;
import org.junit.jupiter.api.Test;
import subscription.SubscriptionRestController;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionRestControllerTests {
    SubscriptionRestController subscriptionRestController =new  SubscriptionRestController();
   // @TestHTTPEndpoint(SubscriptionRestController.class)
   // @TestHTTPResource
    URI url;
    @Test
    void testAddingUsersDetails(){

        Users store = new Users();
        store.set_id("123");
        store.setName("drnxumza");

            assertNotNull(subscriptionRestController.addingUsersDetails(store));
            assertEquals("drnxumza",store.getName());
        }

    @Test
    void givenValidCart_whenStoreToCart_thenSuccessfullyAdded(){
        Products product =new Products();
        product.setCategory("test");
        product.setDescription("test");
        product.setTitle("test");

        assertNotNull(subscriptionRestController.storeToCart(product));
        assertEquals(subscriptionRestController.storeToCart(product).getCategory(),("test"));

    }


    }


