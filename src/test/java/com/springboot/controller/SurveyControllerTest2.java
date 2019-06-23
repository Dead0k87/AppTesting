package com.springboot.controller;

import com.springboot.Question;
import com.springboot.service.SurveyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

//UNIT TEST
@RunWith(SpringRunner.class) // it will run app for the test
@WebMvcTest(value = SurveyController.class, secure = false)
// it will not load all the classes as @ComponentsScan. It will load on this class <<
public class SurveyControllerTest2 {
    //we want to launch specifics Controller we want to test and not the whole application
    @MockBean
    private SurveyService surveyService; // такой же есть в релаьном рабочем классе SurveyController

    @Autowired
    private MockMvc mockMvc; // this will allow us to make call to a services


    @Test
    public void testRetrieveDetailsForQuestion() throws Exception {
        // when

        // surveyService.retrieveQuestion("any","any").thenReturnMockQuestion
        Question mockQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        Mockito.when(surveyService.retrieveQuestion(Mockito.anyString(), Mockito.anyString())).
                thenReturn(mockQuestion);


        //make a call to a service (GET) -- surveys/Survey1/questions/Question1
        RequestBuilder builder = MockMvcRequestBuilders.get("surveys/Survey1/questions/Question1").
                accept(MediaType.APPLICATION_JSON); // вставляем ссылку на запрос GET который вернет Вопрос1
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();
        String actual = mvcResult.getResponse().getContentAsString(); // return JSON GET request Response

        String expected = "{id:Question1,description:Largest Country in the World,correctAnswer:Russia}";
        JSONAssert.assertEquals(expected, actual, false); // assert JSON
        //assert
        // expect some result
    }

    @Test
    public void retrieveSurveyQuestions() throws Exception {
        List<Question> mockList = Arrays.asList(
                new Question("Question1", "First Alphabet", "A", Arrays.asList(
                        "A", "B", "C", "D")),
                new Question("Question2", "Last Alphabet", "Z", Arrays.asList(
                        "A", "X", "Y", "Z")));
        Mockito.when(surveyService.retrieveQuestions(Mockito.anyString())).thenReturn(mockList);

        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get("/surveys/Survey1/questions").accept(
                                MediaType.APPLICATION_JSON))
                .andReturn(); //andExpect(status().isOk()).

        String expected = "[{\"id\":\"Question1\",\"description\":\"First Alphabet\",\"correctAnswer\":\"A\",\"options\":[\"A\",\"B\",\"C\",\"D\"]},{\"id\":\"Question2\",\"description\":\"Last Alphabet\",\"correctAnswer\":\"Z\",\"options\":[\"A\",\"X\",\"Y\",\"Z\"]}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void createSurveyQuestion() throws Exception {
        Question mockQuestion = new Question("1", "Smallest Number", "1",
                Arrays.asList("1", "2", "3", "4"));

        String question = "{\"description\":\"Smallest Number\",\"correctAnswer\":\"1\",\"options\":[\"1\",\"2\",\"3\",\"4\"]}";
        //send this queston as a body to addSurveyQuestion to /surveys/Survey1/questions/
        Mockito.when(surveyService.addQuestion(Mockito.anyString(), Mockito.any(Question.class))).thenReturn(mockQuestion);

        //post!!! (and will return a status)
        RequestBuilder builder = MockMvcRequestBuilders.post("/surveys/Survey1/questions/").content(question).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(builder).andReturn();


        Assert.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        //java.lang.AssertionError:
        //Expected :201 CREATED
        //Actual   :org.springframework.mock.web.MockHttpServletResponse@5111de7c

        //Assert.assertEquals("http://localhost/surveys/Survey1/questions/1",result.getResponse().getHeader(HttpHeaders.LOCATION));
    }
}




