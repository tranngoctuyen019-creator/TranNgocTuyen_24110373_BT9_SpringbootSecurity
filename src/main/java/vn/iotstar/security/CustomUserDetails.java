package vn.iotstar.security;

import lombok.Getter;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.iotstar.entity.User;
import java.io.Serial;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    @Serial private static final long serialVersionUID = 1L;
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final String fullName;
    private final String images;
    private final String role;
    private final boolean enabled;

    public CustomUserDetails(User user) {
        this.id = user.getId(); this.username = user.getUsername(); this.email = user.getEmail();
        this.password = user.getPassword(); this.fullName = user.getFullName(); this.images = user.getImages();
        this.role = user.getRole().getName(); this.enabled = user.isEnabled();
    }
    @Override public java.util.Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority(role)); }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
