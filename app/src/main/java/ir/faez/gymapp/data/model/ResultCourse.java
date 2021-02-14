package ir.faez.gymapp.data.model;

import java.util.List;

public class ResultCourse {
    public List<Course> results;

    public ResultCourse(List<Course> results) {
        this.results = results;
    }

    public List<Course> getResults() {
        return results;
    }

    public void setResults(List<Course> results) {
        this.results = results;
    }
}