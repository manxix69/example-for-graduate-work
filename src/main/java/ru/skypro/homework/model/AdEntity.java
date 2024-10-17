package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ads")
public class AdEntity extends ModelEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false, length = 250)
    private String description;

    @OneToOne
    @JoinColumn(name = "photo", nullable = false)
    private PhotoEntity photo;

    @ManyToOne (fetch=FetchType.LAZY , cascade=CascadeType.ALL)
    @JoinColumn(name = "author", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private Collection<CommentEntity> comments;

    @Column(name = "file_path")
    private String filePath; //путь на ПК
}
