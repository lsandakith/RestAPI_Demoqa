import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;


import static io.restassured.http.ContentType.JSON;

public class BaseTest {

    public final RequestSpecification requestSpec = RestAssured.given();
    @BeforeMethod
    public void setRequestBookDetails()
    {
        requestSpec
                .contentType(JSON)
                .accept(JSON)
                .baseUri(Constants.API_BASE_URL);
    }


}
