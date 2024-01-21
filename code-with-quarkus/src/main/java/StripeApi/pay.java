package StripeApi;

import com.stripe.model.Customer;
import Model.Users;

public interface pay {

    public Customer AddCustomer(Users details);
    public void CheckoutSession();

}
