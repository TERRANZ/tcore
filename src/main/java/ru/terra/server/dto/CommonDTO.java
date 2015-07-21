package ru.terra.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class CommonDTO {
    public String errorMessage = "";
    public Integer errorCode = 0;
    public String status = "";
    public Long timestamp = new Date().getTime();
    public Integer id = -1;

    public CommonDTO() {
    }
}
