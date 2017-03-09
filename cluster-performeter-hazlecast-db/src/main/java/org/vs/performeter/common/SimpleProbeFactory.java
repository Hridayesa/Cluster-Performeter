package org.vs.performeter.common;

import com.tmax.tibero.jdbc.data.DataType;

import java.time.LocalDateTime;

public class SimpleProbeFactory implements ProbeFactory<Probe> {
    ThreadLocal<Probe> probe = new ThreadLocal<Probe>() {
        @Override
        protected Probe initialValue() {
            return new Probe();
        }
    };

    @Override
    public Probe create() {
        Probe p = this.probe.get();
        probe.set(new Probe());
        return p;
    }

    @Override
    public void setTimestamp(String columnName, LocalDateTime localDateTime) {
        setObject(columnName, localDateTime, DataType.TIMESTAMP);
    }

    @Override
    public void setNull(String columnName, int type) {
        setObject(columnName, null, type);
    }

    @Override
    public void setObject(String columnName, Object object, int columnType) {
        try {
            Probe probe = this.probe.get();
            switch (columnName.toLowerCase()) {
                case "env_timein":
                    probe.timein = object;
                    return;
                case "txn_f2":
                    probe.f2 = String.valueOf(object);
                    return;
                case "txn_f3":
                    probe.f3 = String.valueOf(object);
                    return;
                case "txn_f4":
                    probe.f4 = String.valueOf(object);
                    return;
                case "txn_f32":
                    probe.f32 = String.valueOf(object);
                    return;
                case "txn_f37":
                    probe.f37 = String.valueOf(object);
                    return;
                case "txn_f38":
                    probe.f38 = String.valueOf(object);
                    return;
                case "txn_f39":
                    probe.f39 = String.valueOf(object);
                    return;
                case "txn_f41":
                    probe.f41 = String.valueOf(object);
                    return;
                case "txn_f42":
                    probe.f42 = String.valueOf(object);
                    return;
                case "txn_f62_2":
                    probe.f62_2 = String.valueOf(object);
                    return;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
