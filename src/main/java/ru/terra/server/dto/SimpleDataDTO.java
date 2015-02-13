package ru.terra.server.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SimpleDataDTO<T> extends CommonDTO {
    public SimpleDataDTO() {
    }

    public T data;

    public SimpleDataDTO(T data) {
        this.data = data;
    }
}
