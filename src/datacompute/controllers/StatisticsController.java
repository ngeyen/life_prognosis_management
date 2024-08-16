package datacompute.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import accounts.models.Patient;

public class StatisticsController {
      private List<Patient> patients;

    public StatisticsController(List<Patient> patients) {
        this.patients = patients;
    }

    public double calculateAverageAge() {
        return patients.stream()
                .mapToInt(patient -> patient.calculateAge())
                .average()
                .orElse(0.0);
    }

    public double calculateMedianAge() {
        List<Integer> ages = patients.stream()
                .map(patient -> patient.calculateAge())
                .sorted()
                .collect(Collectors.toList());

        int size = ages.size();
        if (size == 0)
            return 0.0;

        if (size % 2 == 0) {
            return (ages.get(size / 2 - 1) + ages.get(size / 2)) / 2.0;
        } else {
            return ages.get(size / 2);
        }
    }

    public double calculateAverageDurationOnArt() {
        return patients.stream()
                .filter(Patient::isOnArt)
                .mapToLong(patient -> calculateDaysBetween(patient.getDiagnosisDate(), patient.getArtStartDate()))
                .average()
                .orElse(0.0);
    }

    public double calculateMedianDurationOnArt() {
        List<Long> durations = patients.stream()
                .filter(Patient::isOnArt)
                .map(patient -> calculateDaysBetween(patient.getDiagnosisDate(), patient.getArtStartDate()))
                .sorted()
                .collect(Collectors.toList());

        int size = durations.size();
        if (size == 0)
            return 0.0;

        if (size % 2 == 0) {
            return (durations.get(size / 2 - 1) + durations.get(size / 2)) / 2.0;
        } else {
            return durations.get(size / 2);
        }
    }

    public Map<String, Long> countPatientsByCountry() {
        return patients.stream()
                .collect(Collectors.groupingBy(Patient::getCountryCode, Collectors.counting()));
    }

    private long calculateDaysBetween(LocalDate start, LocalDate end) {
        return start != null && end != null ? ChronoUnit.DAYS.between(start, end) : 0;
    }
}
