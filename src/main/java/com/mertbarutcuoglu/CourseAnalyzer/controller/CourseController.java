package com.mertbarutcuoglu.CourseAnalyzer.controller;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mertbarutcuoglu.CourseAnalyzer.exceptions.NotEnoughDataException;
import com.mertbarutcuoglu.CourseAnalyzer.model.Course;
import com.mertbarutcuoglu.CourseAnalyzer.model.CourseDetailsParser;
import com.mertbarutcuoglu.CourseAnalyzer.model.DataRetriever;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Controller for the API
@RestController
public class CourseController {
    private static String apiBaseURL = "https://ubcgrades.com/api/v1/grades/UBCV/"; // API URL to perform requests;
    private List<Integer> gradeDistributions;
    private List<Double> fiveYearAverage;

    @CrossOrigin
    @GetMapping("/analyze")
    public Course analyzeCourse(@RequestParam(value = "courseID") String courseID,
                                @RequestParam(value = "courseNumber") String courseNumber,
                                @RequestParam(value = "courseSection") String courseSection,
                                @RequestParam(value = "isWinter") boolean isWinter)
            throws IOException, ParseException, NotEnoughDataException {
        courseID = courseID.toUpperCase();
        String profName = retrieveProfName(courseID, courseNumber, courseSection, isWinter);
        retrieveCourseData(courseID, courseNumber, profName);
        return new Course(courseID, courseNumber, courseSection, profName, fiveYearAverage, gradeDistributions);
    }

    // EFFECTS: retrieves the name of the professor for given course
    private String retrieveProfName(String courseID, String courseNumber, String courseSection, boolean isWinter) throws IOException {
        CourseDetailsParser parser = new CourseDetailsParser();
        DataRetriever retriever = new DataRetriever();

        HtmlPage profNamePage = retriever.retrieveProfName(courseID, courseNumber, courseSection, isWinter);
        String profName = parser.parseProfName(profNamePage);
        return profName;
    }

    // EFFECTS: retrieves the five year average of the given course
    private List<Double> retrieveFiveYearAverage(String courseID, String courseNumber, String profName)
            throws IOException, ParseException {
        CourseDetailsParser parser = new CourseDetailsParser();
        DataRetriever retriever = new DataRetriever();


        // Requests average for five winter terms from 2014 to 2019, not including 2019
        for (int i = 2014; i < 2019; i++) {
            String term = i + "W";
            String apiUrl = apiBaseURL + term + "/" + courseID + "/" + courseNumber;
            String apiResponse = retriever.getResponseAsStringFromURL(apiUrl);

        }
        return fiveYearAverage;
    }

    // EFFECTS: retrieves the grade distribution and the five year average of the given course over the past five years
    private void retrieveCourseData(String courseID, String courseNumber, String profName)
            throws IOException, ParseException {
        CourseDetailsParser parser = new CourseDetailsParser();
        DataRetriever retriever = new DataRetriever();

        Map<String, Integer> gradeDistributions = new LinkedHashMap<>();
        List<Double> fiveYearAverage = new ArrayList<>();

        // Requests average for five winter terms from 2014 to 2019, not including 2019
        for (int i = 2014; i < 2019; i++) {
            String term = i + "W";
            String apiUrl = apiBaseURL + term + "/" + courseID + "/" + courseNumber;
            String apiResponse = retriever.getResponseAsStringFromURL(apiUrl);
            parser.parseGradeDistribution(apiResponse, profName, gradeDistributions);
            List<Double> termAverages = parser.parseAverage(apiResponse, profName);
            fiveYearAverage.addAll(termAverages);
        }

        this.gradeDistributions = new ArrayList<>(gradeDistributions.values());
        this.fiveYearAverage = fiveYearAverage;
    }
}
