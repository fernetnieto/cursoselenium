package ExamenFinalMarzo;

import org.testng.annotations.Factory;

public class mailchimpFactory {
    @Factory
    public Object[] mailchimpFactory(){
        return new Object[]{
                new prueba_mailchimp(),
                new prueba_mailchimp(),
        };
    }
}
