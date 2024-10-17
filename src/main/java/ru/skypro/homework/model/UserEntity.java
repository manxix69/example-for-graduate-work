package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends ModelEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

/*    @Column(name = "email", unique = true, nullable = false)
    private String email;*/

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


//    @OneToOne
    @JsonBackReference
    @JsonIgnore
    @Transient
    private PhotoEntity photo;

    @OneToMany(mappedBy = "author")
    private Collection<AdEntity> ads;

    @OneToMany(mappedBy = "author")
    private Collection<CommentEntity> comments;

    @Column(name = "file_path")
    private String filePath; //путь на ПК

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", photo=" + photo +
                ", ads=" + ads +
                ", comments=" + comments +
                '}';
    }
}
