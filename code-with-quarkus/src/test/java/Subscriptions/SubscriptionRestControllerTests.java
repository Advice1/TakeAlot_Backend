package Subscriptions;

import Model.Products;
import Model.Users;
import io.quarkus.test.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.SubscriptionRestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionRestControllerTests {

    @Mock
    ArrayList<String> products;
    SubscriptionRestController subscriptionRestController =new  SubscriptionRestController();
   // @TestHTTPEndpoint(SubscriptionRestController.class)
   // @TestHTTPResource
    URI url;
    @Test
    void givenUser_whenAddingUsersDetails_thenReturnSuccess(){

        Users store = new Users();
        store.set_id("123");
        store.setName("drnxumza");

            assertNotNull(subscriptionRestController.addingUsersDetails(store));
            assertEquals("drnxumza",store.getName());
        }

    @Test
    void givenValidCart_whenStoreToCart_thenReturnSuccess(){
        Products product =new Products();
        product.setCategory("test");
        product.setDescription("test");
        product.setTitle("test");

        assertNotNull(subscriptionRestController.storeToCart(product));
        assertEquals(subscriptionRestController.storeToCart(product).getCategory(),("test"));

    }
    @Test
    void givenValidUsernameAndPassword_whenCheckProductExist_thenReturnSuccess(){

        Products product = new Products();
        product.setUser("test");
        product.setId("test");
        product.setCategory("test");
        product.setDescription("test");
        product.setTitle("test");

        // create a sample cart
        List<Products> cart = new ArrayList<>();
        cart.add(product);


        System.out.println(product.getUser());
        assertTrue(subscriptionRestController.CheckProductExist("test","test"));

    }
}


