import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;


public class RestAPITesting extends BaseTest{

    private String isbn=null;

    @Test(priority = 1,enabled = true)
    public void testActivityRetrieval() {

        RequestSpecification h = given();
        Response res = h.get("https://demoqa.com/BookStore/v1/Books");
        ResponseBody b = res.getBody();
        String responseBody = b.asString();
        JsonPath jsnPath = res.jsonPath();

        System.out.println(responseBody);
        isbn = jsnPath.get("books.isbn[2]");
        System.out.println("ISBN: " + isbn);


    }

    @Test(priority = 2,enabled = true)
    public void testActivityCreation() {
        System.out.println("ISBN ****" + isbn);
        JSONObject collectionOfIsbns = new JSONObject();
        collectionOfIsbns.put("isbn",isbn);
        JSONArray array = new JSONArray();
        array.put(collectionOfIsbns);
        JSONObject  child = new JSONObject();
        child.put("userId", "871380ef-9c7a-4442-8439-a7ed34ee381d");
        child.put("collectionOfIsbns",array);

        Response response =given()
          .auth()
          .preemptive()
          .basic("ls", "123@bstBST")
          .header("Accept", ContentType.JSON.getAcceptHeader())
          .contentType(ContentType.JSON)
          .body(child.toString()).log().all()
          .post("https://demoqa.com/BookStore/v1/Books")
          .then().extract().response();
           System.out.println(response.getStatusCode());
           Assert.assertEquals(response.getStatusCode(),201,"ISBN already present in the User's Collection!");

           if(response.getStatusCode()==201)
            {
                System.out.println("Book has been added to the collection");
            }

    }
}
