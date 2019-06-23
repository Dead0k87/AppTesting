package com.springboot.controller;

import com.springboot.Question;
import com.springboot.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    ///survey/{surveyId}/questions
    @RequestMapping(value = "surveys/{surveyId}/questions", method = RequestMethod.GET)
    // или @GetMapping ("survey/{surveyId}/questions")
    public List<Question> retrieveQuestionsForSurvey(@PathVariable String surveyId) {
        return surveyService.retrieveQuestions(surveyId);
    }

    // @RequestMapping(value = "surveys/{surveyId}/questions", method = RequestMethod.POST)
    @PostMapping(value = "/surveys/{surveyId}/questions")
    public ResponseEntity<?> addQuestionToSurvey(@PathVariable String surveyId, @RequestBody Question newQuestion) {
        // @ResponseBody  - whateve JSON object that will come with a POST method will be automatically mapped to this Question Object(JSON)

        //what should be structure of  request body?
        // how it will be mapped to question object?
        // what should be returned?
        // what should be response status?

        Question question = surveyService.addQuestion(surveyId, newQuestion); //adding question and getting question ID back
        if (question == null) {
            //return another Response status
            return ResponseEntity.noContent().build();
        }

        //success - URI of the resourse in Response Header
        //Status - created(resourse is ccreated)
        // URI -> /surveys/{surveyId}/questions/{questionId} question.getQuestionId();
        // surveyId and questionId should be replaces with questionId
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(question.getId()).toUri();// it will take current request URI  and append it with /id and will replace /id with question.getId()

        //replacing new Question ID id to URL and creating a location.
        //we will get complete URI for created resource
// {id} будет заменено на результат метода question.getId()
        //Status
        return ResponseEntity.created(location).build(); // return status! which we are giving back

    }

    @GetMapping(value = "surveys/{surveyId}/questions/{questionId}")
    public Question retrieveDetailsForQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        return surveyService.retrieveQuestion(surveyId, questionId);
    }


}
