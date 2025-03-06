package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import tn.esprit.entities.Performance;

public class StatsController {

    @FXML
    private LineChart<String, Number> performanceLineChart;

    public void displayPerformanceStats(Performance performance) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Performance Trends");

        series.getData().add(new XYChart.Data<>("Speed", performance.getSpeed()));
        series.getData().add(new XYChart.Data<>("Agility", performance.getAgility()));
        series.getData().add(new XYChart.Data<>("Goals", performance.getNbr_goals()));
        series.getData().add(new XYChart.Data<>("Assists", performance.getAssists()));
        series.getData().add(new XYChart.Data<>("Fouls", performance.getNbr_fouls()));

        performanceLineChart.getData().clear();
        performanceLineChart.getData().add(series);
    }
}
