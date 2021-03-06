package lab_6;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.IncorrectPost;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class NegativeApiTests {
    private String token;

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "https://gorest.co.in";
        token = "6vvgilJcmVZRZGQc3KVA5iutNQTx_YoOsYv1";
    }

    //GET /public-api/users без указания токена авторизации и с некорректным
    //токеном
    @Test
    public void token_empty_uncorrect()
    {
        String token_empty = "";
        System.out.println("Тест 1");
        System.out.println("Пустой токкен.");
        given().auth().oauth2(token_empty)
                .when().get("public-api/users")
                .then().log().all()

                .statusCode(200);

        String token_uncorrect = "1234";
        System.out.println("Некорректный токкен.");
        given().auth().oauth2(token_empty)
                .when().get("public-api/users")
                .then().log().all()

                .statusCode(200);
    }


    //POST /public-api/users с некорректным форматом тела запроса
    @DataProvider(name = "data_incorrect_post")
    public Object[][] data_incorrect_post()
    {
        return new Object[][] {{"Snape", "male", "Snappy@gmail.com", "+7 (900) 696-99-66"}};
    }
    @Test(dataProvider = "data_incorrect_post")
    public void test_uncorrect_post(String last_name, String gender, String email, String phone)
    {
        IncorrectPost post1 = new IncorrectPost(last_name,gender, email, phone);
        System.out.println("Тест 2");
        System.out.println("POST с некорректным форматом тела запроса.");
        given().auth().oauth2(token)
                .log().all()
                .contentType(ContentType.JSON)
                .body(post1)
                .when()
                .post( "/public-api/users")
                .then()
                .log().all()
                .assertThat()
                .body("result.last_name", equalTo(post1.getLast_name()))
                .body("result.gender", equalTo(post1.getGender()))
                .body("result.email", equalTo(post1.getEmail()))
                .body("result.phone", equalTo(post1.getPhone()));
    }

    //DELETE /public-api/users
    @Test
    public void test_delete_ID() {
        Integer id = 29177;
        System.out.println("Тест 3");
        System.out.println("DELETE /public-api/users.");
        given().auth().oauth2(token)
                .contentType(ContentType.JSON)
                .when().delete("public-api/users/" + id)
                .then().log().all()
                .body("result", equalTo(null))
                .statusCode(200);
    }
}
