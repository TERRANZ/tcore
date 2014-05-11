package ru.terra.server.dto;

import flexjson.JSONDeserializer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SimpleDataDTO<T> extends CommonDTO {
    public SimpleDataDTO() {
    }

    public T data;

    public SimpleDataDTO(T data) {
        this.data = data;
    }

    public static SimpleDataDTO valueOf(String json) {
        return new JSONDeserializer<SimpleDataDTO>().deserialize(json);
    }
}
