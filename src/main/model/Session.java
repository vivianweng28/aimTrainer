package model;

public interface Session {

    void analyze(double x, double y, double centerX, double centerY, double radius);

    Suggestion getLastSuggestion();

    Suggestion getSummarySuggestion();

    Suggestion updateSummarySuggestion();

}