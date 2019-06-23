package com.springboot.controller;

import com.springboot.Application;
import com.springboot.Question;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class) // it will run app for the test
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// SpringBootTest will help in launching application for a test
// SpringBootTest.WebEnvironment.RANDOM_PORT for random port
public class SurveyControllerTest {

    @LocalServerPort // this will tell us what port was used for the app
    private int port;
    private String question;

    TestRestTemplate template = new TestRestTemplate();
    HttpHeaders header = new HttpHeaders();//new HttpHeaders();

    private String createHttpHeaders(String userID, String password) {
        HttpHeaders headers = new HttpHeaders();
        //add loginPass details

        // "Authorization", "Basic" + Base64Encoding(userID append user with ":" and with password)
        String loginPass = userID + ":" + password; // merging user:pass
        byte[] encodeAuth = Base64.encodeBase64(loginPass.getBytes(Charset.forName("US-ASCII"))); //convert a String into Base64 encoding
        String headerValue = "Basic" + new String(encodeAuth);
        return headerValue;
    }


    private String createURLWithPort(String retrieveSpecificQuestion) {
        return "http://localhost:" + port + retrieveSpecificQuestion;
    }

    @Before
    public void before() {
        header.add("Authorization", createHttpHeaders("user1", "secret1"));
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testjsonAssert() throws JSONException {
        String actual = "{id:1,name:Ranga, role:Admin}";
        JSONAssert.assertEquals("{id:1, role:Admin}", actual, false); // compares two JSON. JSON ignores spaces
        //JSONAssert.assertEquals("{id:2}", "{id:1,name:Ranga}", false); // will fail
        // strict (true/false) будет определять все ли поля объекта json должны совпадать или нет
    }

    @Test
    public void testRetrieveQuestion2() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        for (int i = 1; i <= 4; i++) {
            question = "Question" + i;
            String url = "http://localhost:" + port + "/surveys/Survey1/questions/" + question;

            HttpEntity<?> entity = new HttpEntity<>(null, header);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            System.out.printf(response.getBody());
        }
    }

    @Test
    public void testRetrieveSurveyQuestion() throws JSONException {
        String retrieveSpecificQuestion = "/surveys/Survey1/questions/Question1";
        String url = createURLWithPort(retrieveSpecificQuestion);
        //because port is different
        System.out.println("Used port: " + port);

        // invoke URL
        // String output = testRestTemplate.getForObject(url, String.class);// execute URL and will try to convert to a class we would give it

        // Passing a header for JSON -- application/json
        // HttpEntity has a header component

        HttpEntity<?> entity = new HttpEntity<String>(null, header);//create (body,header)
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);//output a String

        System.out.printf("Response: " + response.getBody());
        String expected = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\",\"correctAnswer\":\"Russia\",\"options\":[\"India\",\"Russia\",\"United States\",\"China\"]}";
        //Assert.assertTrue(response.getBody().contains("\"id\":\"Question1\""));
        String expected2 = "{\"id\":\"Question1\",\"description\":\"Largest Country in the World\"}";
        JSONAssert.assertEquals(expected2, response.getBody(), false);
    }


    @Test
    public void containsQuestion() {
        Question sampleQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        HttpEntity<?> entity = new HttpEntity<>(null, header);

        String retrieveAllTheQuestions = "/surveys/Survey1/questions/";
        ResponseEntity<List<Question>> response = template.exchange(createURLWithPort(retrieveAllTheQuestions), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Question>>() {
        });
        Assert.assertEquals(true, response.getBody().contains(sampleQuestion));
    }

    @Test
    public void createSurveyQeustion() {
        Question question1 = new Question("DOESN'T MATTER", "Smallest Number",
                "1", Arrays.asList("1", "2", "3", "4"));

        HttpEntity<?> entity = new HttpEntity<Question>(question1, header);// now body is a question, header is a Post method
        ResponseEntity<String> response = template.exchange(createURLWithPort("/surveys/Survey1/questions/"), HttpMethod.POST, entity, String.class);
        //when it posts it will return new Object location URI (url)
        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);//get first location, get POSted in Body header of an object/ вернет только 1 хедер потому чт одоабвили 1 объект
        System.out.println("added question location: " + actual);
        TestRestTemplate restTemplate2 = new TestRestTemplate();
        restTemplate2.exchange(createURLWithPort("/surveys/Survey1/questions/"), HttpMethod.GET, entity, String.class);

        //System.out.println("Added: " + responseGet);

        Assert.assertTrue(actual.contains("/surveys/Survey1/questions/"));
    }
}


