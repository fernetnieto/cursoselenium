package ExamenFinalMarzo;

import org.testng.annotations.DataProvider;

public class dataProvider {

    @DataProvider(name="datos")
    public Object[] emails(){
        return new Object[]{
                "test1@test.com",
                "test2@test.com",
                "test3@test.com"
        };
    }
}
