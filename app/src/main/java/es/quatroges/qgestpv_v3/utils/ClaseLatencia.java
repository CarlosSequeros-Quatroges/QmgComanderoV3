package es.quatroges.qgestpv_v3.utils;

import java.util.ArrayList;

public class ClaseLatencia{
    private Long ultima;
    private Long media;
    private Long maxima;

    private boolean correcto;

    public Long getUltima() {
        return ultima;
    }

    public Long getMedia() {
        return media;
    }

    public Long getMaxima() {
        return maxima;
    }

    public ArrayList<Long> getLatencias() {
        return latencias;
    }

    public boolean isCorrecto() {
        return correcto;
    }

    private ArrayList<Long> latencias;
    public ClaseLatencia() {
        this.ultima = Long.valueOf(0);
        this.media = Long.valueOf(0);
        this.maxima = Long.valueOf(0);
        this.correcto = true;
    }

    public void add(Long ini, Long fin, boolean correcto){
        long lat = fin - ini;
        if (latencias ==  null) {
            latencias = new ArrayList<>();
        }
        else if (latencias.size() > 30){
            latencias.remove(0);
        }
        latencias.add(lat);
        ultima = lat;
        maxima =  latencias.stream().max(Long::compare).get();
        media = Double.valueOf(latencias.stream().mapToLong(Long::longValue ).summaryStatistics().getAverage()).longValue();
        this.correcto = correcto;

    }

}
