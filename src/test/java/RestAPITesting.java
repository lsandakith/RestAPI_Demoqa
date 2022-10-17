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

public class RestAPITesting {

    private String isbn=null;
    public String ACTIVITY_END_POINT ="https://demoqa.com/BookStore/v1/Books";

    @Test(priority = 1,enabled = true)
    public void testActivityRetrieval() {

        RequestSpecification h = given();
        Response res = h.get(ACTIVITY_END_POINT);
        ResponseBody b = res.getBody();
        String responseBody = b.asString();
        JsonPath jsnPath = res.jsonPath();

        System.out.println(responseBody);
        isbn = jsnPath.get("books.isbn[7]");
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
        child.put("userId", "cb211ff9-30ed-49d4-bfe8-01d5a49383cf");
        child.put("collectionOfIsbns",array);

        Response response =given()
          .auth()
          .preemptive()
          .basic("apiTestUser", "123@bstBST")
          .header("Accept", ContentType.JSON.getAcceptHeader())
          .contentType(ContentType.JSON)
          .body(child.toString()).log().all()
          .post(ACTIVITY_END_POINT)
          .then().extract().response();
           System.out.println("Response code : "+response.getStatusCode());
           Assert.assertEquals(response.getStatusCode(),201,"ISBN already present in the User's Collection!");

           if(response.getStatusCode()==201)
            {
                System.out.println("Book has been added to the collection");
            }

    }
}
