package com.objectvault.objectvault.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name="users", schema = "objectvault")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserEntity implements UserDetails {

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
    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

    @NonNull
    @Column(name="email")
    private String email;

    @NonNull
    @Column(name="password")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
