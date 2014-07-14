package ru.terra.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class ListDTO<T> extends CommonDTO {
    public List<T> data = new ArrayList<>();
    private Integer size;

    public ListDTO() {
    }

    public Integer getSize() {
        size = data.size();
        return size;
    }

    public void setData(List<T> d) {
        this.data = d;
    }

}
