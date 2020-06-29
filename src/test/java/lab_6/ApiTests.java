package lab_6;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Post;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ApiTests {

    private String token;

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "https://gorest.co.in";
        token = "6vvgilJcmVZRZGQc3KVA5iutNQTx_YoOsYv1";
    }
    //получить список всех пользователей
    @Test
    public void all_list_users() {
        System.out.println("Тест 1");
        System.out.println("Получить список всех пользователей.");
        given()
                .auth().oauth2(token)
                .log().all()
                .when()
                .request("GET","public-api/users")
                .then()
                .log().status()
                .statusCode(200);
        System.out.println("");
    }

    //получить список пользователей с указанным именем
    @Test
    public void list_name_user()
    {
        System.out.println("Тест 2");
        System.out.println("Получить список пользователей с указанным именем.");
        given()
                .auth().oauth2(token)
                .when()
                .request("GET", "/public-api/users?access-token=" + token + "&first_name=Jerad")
                .then()
                .log().body()
                .statusCode(200);
    }

    //создать нового пользователя
    @DataProvider(name = "data_create_new_user")
    public Object[][] data_create_new_user()
    {
        return new Object[][] {{"Severus", "Snape", "male", "Snappy@gmail.com", "+7 (900) 696-99-66"}};
    }



    @Test(dataProvider = "data_create_new_user")
    public void test_create_new_user( String first_name, String last_name, String gender, String email, String phone)
    {
        System.out.println("Тест 3");
        System.out.println("Cоздать нового пользователя.");
        Post Post1 = new Post(first_name, last_name,  gender, email, phone);
        given().auth().oauth2(token)
                .log().all()
                .contentType(ContentType.JSON)
                .body(Post1)
                .when()
                .post( "/public-api/users")
                .then()
                .log().all()
                .assertThat()
                .body("result.first_name", equalTo(Post1.getFirst_name()))
                .body("result.last_name", equalTo(Post1.getLast_name()))
                .body("result.gender", equalTo(Post1.getGender()))
                .body("result.email", equalTo(Post1.getEmail()))
                .body("result.phone", equalTo(Post1.getPhone()))
                .statusCode(302);

    }

    //получить пользователя по его ID
    @Test
    public void get_user_ID()
    {
        String id = "1685";
        System.out.println("Тест 4");
        System.out.println("Получить пользователя по его ID.");
        given()
                .auth().oauth2(token)
                .when()
                .request("GET", "/public-api/users/" + id +"?access-token=" + token)
                .then()
                .log().body()
                .statusCode(200);
    }

    //Изменить пользователя с указанным ID
    Integer id = 29080;
    //Меняем номер телефона
    @DataProvider(name = "data_change_user")
    public Object[][] data_change_ID()
    {
        return new Object[][] {{id, "Severus", "Snape", "male", "Snappy@gmail.com", "+8 (900) 877-77-88"}};
    }
    @Test(dataProvider = "data_change_user")
    public void put_user_ID(int id,String first_name, String last_name, String gender, String email, String phone )
    {
        System.out.println("Тест 5");
        System.out.println("Изменить пользователя с указанным ID.");
        Post Post2 =  new Post(first_name, last_name,  gender, email, phone);
        given().auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(Post2)
                .when().put("public-api/users/" + id)
                .then()
                .log().all()
                .statusCode(200);
    }

    //удалить пользователя с указанным ID
    @Test
    public void test_delete_ID() {
        Integer id = 29163;
        System.out.println("Тест 6");
        System.out.println("Удалить пользователя с указанным ID.");
        given().auth().oauth2(token)
                .contentType(ContentType.JSON)
                .when().delete("public-api/users/" + id)
                .then().log().all()
                .body("result", equalTo(null))
                .statusCode(200);
    }
}
