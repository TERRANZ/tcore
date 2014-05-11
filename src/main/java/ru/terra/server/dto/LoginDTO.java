package ru.terra.server.dto;

import flexjson.JSONDeserializer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginDTO extends CommonDTO {
    public String message = "";
    public boolean logged;
    public String session = "";

    public LoginDTO() {
    }

    public static LoginDTO valueOf(String json) {
        return new JSONDeserializer<LoginDTO>().deserialize(json);
    }

}
