package org.vs.performeter.common;

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
        probe.set(null);
        return p;
    }

    @Override
    public void setTimestamp(String columnName, LocalDateTime localDateTime) {
        probe.get().timein = localDateTime;
    }

    @Override
    public void setNull(String columnName, int type) {
        setObject(columnName, null, type);
    }

    @Override
    public void setObject(String columnName, Object object, int columnType) {
        switch (columnName) {
            case "txn_f2":
                probe.get().f2 = String.valueOf(object);
                return;
            case "txn_f3":
                probe.get().f3 = String.valueOf(object);
            case "txn_f4":
                probe.get().f4 = String.valueOf(object);
                return;
            case "txn_f32":
                probe.get().f32 = String.valueOf(object);
                return;
            case "txn_f37":
                probe.get().f37 = String.valueOf(object);
                return;
            case "txn_f38":
                probe.get().f38 = String.valueOf(object);
                return;
            case "txn_f39":
                probe.get().f39 = String.valueOf(object);
                return;
            case "txn_f41":
                probe.get().f41 = String.valueOf(object);
                return;
            case "txn_f42":
                probe.get().f42 = String.valueOf(object);
                return;
            case "txn_f42_2":
                probe.get().f62_2 = String.valueOf(object);
                return;
        }
    }
}
