package requests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.lang.String;

public class AllServices extends JUnit4CitrusTestRunner {

    @Autowired
    HttpClient generalClient;

    String author;
    String title;
    int id;
    String payloadId;


    @Test
    @CitrusTest
    public void getAllBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .get("api/book") //endpoint
                .contentType("application/json"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(200)
                        .status(HttpStatus.NO_CONTENT));
        echo("Liste boş");
        traceVariables();
    }


    @Test
    @CitrusTest
    public void checkAuthorRequirementsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/")
                .payload("author=${author}"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("Yazar alanı boş olamaz");
    }

    @Test
    @CitrusTest
    public void checkTitleRequirementsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/") //endpoint
                .payload("title=${title}"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("Başlık alanı boş olamaz");
    }

    @Test
    @CitrusTest
    public void checkRequirementsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/<book_id>/") //endpoint
                .payload("title=${title}&author=${author}"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("Yazar be başlık bilgisi gerekli");
    }



    @Test
    @CitrusTest
    public void checkDescriptonRequirementsFieldsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/")
                .payload("title=${}&author=${}"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("'Başlık' ve 'Yazar' alanı boş olamaz");
    }


    @Test
    @CitrusTest
    public void addNewBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/") //endpoint
                .payload("title=${title}&author=${author}"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(200)
                        .extractFromPayload("$.Id", payloadId)
                        .extractFromPayload("$.title", title)
                        .extractFromPayload("author", author));

        traceVariables();

    }


    @Test
    @CitrusTest
    public void getSelectedBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .get("/api/books/"+payloadId)); // üstteki servisten alınan id ile istek gönderiliyor
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(200)
                        .status(HttpStatus.FOUND)
                        .validate("${title}", "title" ));
        echo("Aranan kitap bulundu");
        traceVariables();
    }


    @Test
    @CitrusTest
    public void getSelectedNotExistBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .get("/api/books/id"));
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(404)
                        .status(HttpStatus.NOT_FOUND));
        echo("Aranan kitap mevcut değil");
        traceVariables();
    }

}
