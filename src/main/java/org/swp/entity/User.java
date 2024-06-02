package org.swp.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.swp.enums.UserRole;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Entity
@Data
@Table(name = "tbl_user")
public class User extends BaseEntity implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //account information
    @NotNull
    @Column(unique = true)
    private String username;
    private String password;
    private UserRole role;
    //fundamental personal information
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    //other
    @Lob
    private String profileImageUrl;
    @Lob
    private String coverImageUrl;
    private LocalDate birthday;

    @OneToOne(mappedBy = "user")
    private Shop shop;
}
