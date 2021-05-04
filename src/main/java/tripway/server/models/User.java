package tripway.server.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Nikita Burtelov
 */
@Entity
@Table(name = "user")
@Getter
@Setter
public class User extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "mail")
    private String mail;
}

