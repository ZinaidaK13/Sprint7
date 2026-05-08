import data.CourierData;
import io.restassured.RestAssured;
import org.junit.Before;



public class BaseAPITest {

    @Before
    public void setUp(){
        RestAssured.baseURI= CourierData.BASE_API_URL;
    }

}
