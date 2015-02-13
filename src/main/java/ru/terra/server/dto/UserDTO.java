package ru.terra.server.dto;

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
}
