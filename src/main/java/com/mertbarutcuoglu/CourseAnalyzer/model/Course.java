package com.mertbarutcuoglu.CourseAnalyzer.model;

import com.mertbarutcuoglu.CourseAnalyzer.exceptions.NotEnoughDataException;

import java.text.DecimalFormat;
import java.util.List;

public class Course {
    private String courseID;
    private String courseNumber;
    private String courseSection;
    private String profName;

    private List<Integer> gradeDistributions;
    private List<Double> courseAveragesForYears;
    private Double courseFiveYearAverage;

    //EFFECTS: constructs a course with given parameters, and calculates five year average from the given averages
    public Course(String courseID, String courseNum, String courseSection, String profName, List<Double> termAverages,
                  List<Integer> gradeDistributions) throws NotEnoughDataException {
        if (termAverages.size() == 0 || gradeDistributions.size() == 0) {
            throw new NotEnoughDataException();
        }
        this.courseID = courseID;
        this.courseNumber = courseNum;
        this.courseSection = courseSection;
        this.profName = profName;
        this.courseAveragesForYears = termAverages;
        this.courseFiveYearAverage = calculateFiveYearAverage();
        this.gradeDistributions = gradeDistributions;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public String getProfName() {
        return profName;
    }

    public Double getCourseFiveYearAverage() {
        return courseFiveYearAverage;
    }

    public List<Double> getCourseAveragesForYears() {
        return courseAveragesForYears;
    }

    public List<Integer> getGradeDistribution() {
        return gradeDistributions;
    }

    // EFFECTS: calculates and returns the five year average for the course
    private Double calculateFiveYearAverage() {
        double sum = 0.0;
        int count = 0;

        for (int i = 0; i < courseAveragesForYears.size(); i++) {
            if (courseAveragesForYears.get(i) != 0.0) {
                sum = sum + courseAveragesForYears.get(i);
                count++;
            }
        }
        return  Double.parseDouble(new DecimalFormat("#.##").format(sum/count));
    }
}
