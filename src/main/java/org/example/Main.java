package org.example;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Entry> entries = new ArrayList<>();
        setData(entries);

        InconsistencyChecker checker = new InconsistencyChecker();
        List<Entry> checkedEntries = checker.checkInconsistencies(entries);

        for (Entry entry : checkedEntries) {
            System.out.println("---------------"+(entry.isInconsistency()?"---Inconsistência":"Lançamento Manual")+"--------------------");
            System.out.println("Data: 17/05/2023");
            System.out.println("Hora inicial: " + entry.getHrInicial());
            System.out.println("Hora final: " + entry.getHrFinal());
            System.out.println("Tempo total: " + entry.getTotalTime());
            System.out.println("Inconsistência: " + entry.isInconsistency());
            System.out.println("----------------------------------------------------\n");
        }

        Duration totalTimeConsistent = checkedEntries.stream()
                .filter(entry -> !entry.isInconsistency())
                .map(entry -> Duration.between(LocalTime.MIN, entry.getTotalTime()))
                .reduce(Duration.ZERO, Duration::plus);

        Duration totalTimeInconsistent = checkedEntries.stream()
                .filter(Entry::isInconsistency)
                .map(entry -> Duration.between(LocalTime.MIN, entry.getTotalTime()))
                .reduce(Duration.ZERO, Duration::plus);

        Duration totalTimeGeneral = totalTimeConsistent.plus(totalTimeInconsistent);

        System.out.println("Total de horas - Lançamento:    " + formatDuration(totalTimeConsistent));
        System.out.println("Total de horas - Inconsistente: " + formatDuration(totalTimeInconsistent));
        System.out.println("Total de horas - Geral:         "+formatDuration(totalTimeGeneral));
    }

    private static void setData(List<Entry> entries){
        entries.add(new Entry(LocalTime.of(7, 50), LocalTime.of(8, 0), LocalTime.of(0, 10))); // hora extra antes de iniciar o trabalho
        entries.add(new Entry(LocalTime.of(8, 0), LocalTime.of(9, 40), LocalTime.of(1, 40)));
        entries.add(new Entry(LocalTime.of(9, 45), LocalTime.of(10, 0), LocalTime.of(0, 15)));
        entries.add(new Entry(LocalTime.of(10, 0), LocalTime.of(11, 50), LocalTime.of(1, 50)));
        entries.add(new Entry(LocalTime.of(12, 0), LocalTime.of(12, 10), LocalTime.of(0, 10))); // hora extra no almoço
        entries.add(new Entry(LocalTime.of(12, 25), LocalTime.of(12, 30), LocalTime.of(0, 5))); // hora extra no almoço
        entries.add(new Entry(LocalTime.of(13, 12), LocalTime.of(15, 0), LocalTime.of(1, 47)));
        entries.add(new Entry(LocalTime.of(15, 10), LocalTime.of(16, 30), LocalTime.of(1, 20)));
        entries.add(new Entry(LocalTime.of(17, 50), LocalTime.of(18, 00), LocalTime.of(0, 10)));
        entries.add(new Entry(LocalTime.of(18, 10), LocalTime.of(19, 0), LocalTime.of(0, 50))); // hora extra pós trabalho
        entries.add(new Entry(LocalTime.of(19, 0), LocalTime.of(20, 0), LocalTime.of(1, 0))); // hora extra pós trabalho
        entries.add(new Entry(LocalTime.of(20, 0), LocalTime.of(20, 30), LocalTime.of(0, 30))); // hora extra pós trabalho
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
