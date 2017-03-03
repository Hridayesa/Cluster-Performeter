package org.vs.performeter.common;


import java.io.Serializable;
import java.time.LocalDateTime;

public class Probe implements Serializable {
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
    private volatile String key;

    public Object getKey() {
        if (key==null) {
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
