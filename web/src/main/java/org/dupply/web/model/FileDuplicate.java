package org.dupply.web.model;

import java.util.List;

public class FileDuplicate {

    protected String name;
    protected long size;
    protected String sizeName;
    protected List<String> locations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "FileDuplicate{" +
                "name='" + name + '\'' +
                ", sizeName='" + sizeName + '\'' +
                ", size=" + size +
                ", locations=" + locations +
                '}';
    }
}
