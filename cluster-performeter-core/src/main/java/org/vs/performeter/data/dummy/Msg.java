package org.vs.performeter.data.dummy;

import java.io.Serializable;

/**
 * Created by Denis Karpov on 09.12.2016.
 */
public class Msg implements Serializable {
    private static final long serialVersionUID = 1182572849339231923L;

    private String id;
    private String name;
    private String bbb;

    public Msg(String id, String name, String bbb) {
        this.id = id;
        this.name = name;
        this.bbb = bbb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBbb() {
        return bbb;
    }

    public void setBbb(String bbb) {
        this.bbb = bbb;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bbb='" + bbb + '\'' +
                '}';
    }
}
