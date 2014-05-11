package ru.terra.server.dto;

import flexjson.JSONDeserializer;
import ru.terra.server.db.entity.AbstractUser;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO extends CommonDTO {
    public String name;
    public Integer level;

    public UserDTO() {
    }

    public UserDTO(AbstractUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.level = user.getLevel();
    }

    public static UserDTO valueOf(String json) {
        return new JSONDeserializer<UserDTO>().deserialize(json);
    }

}
