package model;

import java.util.List;

public interface Session {

    void analyze(double x, double y, double centerX, double centerY, double radius);

    Suggestion getLastSuggestion();

    Suggestion updateSummarySuggestion();

    List<Suggestion> getAllSuggestions();

    void hit();

    double getAccuracy();

}
