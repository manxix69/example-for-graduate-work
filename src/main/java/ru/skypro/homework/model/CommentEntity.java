package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private Long createdAt;

    @ManyToOne
    @JoinColumn(name = "author")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "ad")
    private AdEntity ad;

    @Override
    public String toString() {
        return "CommentEntity{"
                + "id=" + id +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", author=" + author +
//                ", ad=" + ad +
                '}';
    }
}