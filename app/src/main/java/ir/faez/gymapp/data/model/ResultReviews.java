package ir.faez.gymapp.data.model;

import java.util.List;

public class ResultReviews {
    public List<Review> results;

    public ResultReviews(List<Review> results) {
        this.results = results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}