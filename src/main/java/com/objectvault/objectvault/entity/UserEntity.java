package com.objectvault.objectvault.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user", schema = "objectvault")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(
            name="uuid",
            strategy="uuid2",
            parameters={}
    )

    @NonNull
    @Column(name="userid")
    private String userid;

    @NonNull
    @Column(name="first_name")
    private String first_name;

    @Column(name="last_name")
    private String last_name;

    @NonNull
    @Column(name="email")
    private String email;

    @NonNull
    @Column(name="password")
    private String password;

}
