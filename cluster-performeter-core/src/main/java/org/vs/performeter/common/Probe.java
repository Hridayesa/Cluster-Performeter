package org.vs.performeter.common;


import java.time.LocalDateTime;

public class Probe {
    public LocalDateTime timein;
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

    public Object getKey() {
        return f32+f37+f41+f42;
    }
}
