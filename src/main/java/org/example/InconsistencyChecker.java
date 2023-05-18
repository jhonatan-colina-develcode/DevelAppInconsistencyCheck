package org.example;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class InconsistencyChecker {
    private static final LocalTime START_WORKDAY = LocalTime.of(8, 0);
    private static final LocalTime LUNCH_TIME = LocalTime.of(12, 0);
    private static final LocalTime END_LUNCH = LocalTime.of(13, 12);
    private static final LocalTime END_WORKDAY = LocalTime.of(18, 0);

    public List<Entry> checkInconsistencies(List<Entry> entries) {
        LocalTime expectedStartTime = START_WORKDAY;
        List<Entry> newEntries = new ArrayList<>();

        for (Entry entry : entries) {
            LocalTime hrInicial = entry.getHrInicial();

            // Check if entry has an inconsistency
            if (!hrInicial.equals(expectedStartTime) &&
                    hrInicial.isAfter(START_WORKDAY) &&
                    hrInicial.isBefore(END_WORKDAY)) {
                Entry inconsistentEntry = new Entry();
                inconsistentEntry.setHrInicial(expectedStartTime);

                // Check if inconsistency spans lunch time
                if (expectedStartTime.isBefore(LUNCH_TIME) && hrInicial.isAfter(LUNCH_TIME)) {
                    inconsistentEntry.setHrFinal(LUNCH_TIME);
                } else {
                    inconsistentEntry.setHrFinal(hrInicial);
                }

                inconsistentEntry.setTotalTime(LocalTime.MIN.plusMinutes(ChronoUnit.MINUTES.between(expectedStartTime, inconsistentEntry.getHrFinal())));
                inconsistentEntry.setInconsistency(true);
                newEntries.add(inconsistentEntry);
            }

            newEntries.add(entry);
            expectedStartTime = entry.getHrFinal();

            // Check for lunch time
            if (hrInicial.equals(LUNCH_TIME)) {
                expectedStartTime = END_LUNCH;
            }
        }


        // Check for gap between last entry and end of the workday disregarding start of the overtime
        ListIterator<Entry> iterator = entries.listIterator(entries.size());
        while (iterator.hasPrevious()) {
            Entry entry = iterator.previous();
            LocalTime hrFinal = entry.getHrFinal();

            if(hrFinal.compareTo(END_WORKDAY) == 0)
                break;

            if(hrFinal.isBefore(END_WORKDAY)){
                Entry inconsistentEntry = new Entry();
                inconsistentEntry.setHrInicial(hrFinal);
                inconsistentEntry.setHrFinal(END_WORKDAY);
                inconsistentEntry.setTotalTime(LocalTime.MIN.plusMinutes(ChronoUnit.MINUTES.between(inconsistentEntry.getHrInicial(), END_WORKDAY)));
                inconsistentEntry.setInconsistency(true);
                newEntries.add(inconsistentEntry);
                break;
            }
        }

        // sort by hrInicial
        Collections.sort(newEntries);

        // Remove inconsistency in lunch time
        newEntries.removeIf(entry ->
                entry.isInconsistency() &&
                        entry.getHrInicial().compareTo(LUNCH_TIME) >= 0 &&
                        entry.getHrFinal().compareTo(END_LUNCH) <= 0
        );

        return newEntries;
    }
}
