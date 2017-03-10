package org.vs.performeter.data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Denis Karpov on 06.03.2017.
 */
public class Probe implements Serializable {
    private static final long serialVersionUID = -7230597394330499198L;
    public static final Probe ERROR_PROBE = new Probe();
    public static final Probe END_PROBE = new Probe();

    public Object timein;
    public String f2;
    public String f3;
    public String f4;
    public String f32;
    public String f37;
    public String f38;
    public String f39;
    public String f41;
    public String f42;
    public String f62_2;
    private String key;


    public Probe() {
    }

    public Probe(LocalDateTime timein, String f2, String f3, String f4, String f32, String f37, String f38, String f39, String f41, String f42, String f62_2) {
        this.timein = timein;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f32 = f32;
        this.f37 = f37;
        this.f38 = f38;
        this.f39 = f39;
        this.f41 = f41;
        this.f42 = f42;
        this.f62_2 = f62_2;
        this.key = f32 + f37 + f41 + f42;
    }

    public String getKey() {
        if (key==null){
            key = f32 + f37 + f41 + f42;
        }
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Probe probe = (Probe) o;
        return getKey().equals(probe.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "Probe{" +
                "timein=" + timein +
                ", f2='" + f2 + '\'' +
                ", f3='" + f3 + '\'' +
                ", f4='" + f4 + '\'' +
                ", f32='" + f32 + '\'' +
                ", f37='" + f37 + '\'' +
                ", f38='" + f38 + '\'' +
                ", f39='" + f39 + '\'' +
                ", f41='" + f41 + '\'' +
                ", f42='" + f42 + '\'' +
                ", f62_2='" + f62_2 + '\'' +
                '}';
    }
}
