package org.example;

import java.time.LocalTime;

public class Entry implements Comparable<Entry> {
    private LocalTime hrInicial;
    private LocalTime hrFinal;
    private LocalTime totalTime;
    private boolean inconsistency = false;

    @Override
    public int compareTo(Entry o) {
        return this.hrInicial.compareTo(o.getHrInicial());
    }

    public Entry() {
    }

    public Entry(LocalTime hrInicial, LocalTime hrFinal, LocalTime totalTime) {
        this.hrInicial = hrInicial;
        this.hrFinal = hrFinal;
        this.totalTime = totalTime;
    }

    public LocalTime getHrInicial() {
        return hrInicial;
    }

    public void setHrInicial(LocalTime hrInicial) {
        this.hrInicial = hrInicial;
    }

    public LocalTime getHrFinal() {
        return hrFinal;
    }

    public void setHrFinal(LocalTime hrFinal) {
        this.hrFinal = hrFinal;
    }

    public LocalTime getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(LocalTime totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isInconsistency() {
        return inconsistency;
    }

    public void setInconsistency(boolean inconsistency) {
        this.inconsistency = inconsistency;
    }

}
