package ru.terra.server.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginDTO extends CommonDTO {
    public String message = "";
    public boolean logged;
    public String session = "";

    public LoginDTO() {
    }

    public LoginDTO(String message, boolean logged, String session) {
        this.message = message;
        this.logged = logged;
        this.session = session;
    }
}
