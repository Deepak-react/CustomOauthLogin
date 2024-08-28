package com.atquil.jwt_oauth2.roles;

import lombok.*;
import org.springframework.security.core.authority.*;

import java.util.*;
import java.util.stream.*;

import static com.atquil.jwt_oauth2.roles.Permission.*;

@RequiredArgsConstructor
public enum Role {

    USER(Collections.emptySet()),
    ADMIN(Collections.emptySet()),
    MANAGER(Collections.emptySet());

    @Getter
    private final Set<Permission> permissions;


    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        System.out.println("The authorities are : "+ authorities);
        return authorities;
    }


}
