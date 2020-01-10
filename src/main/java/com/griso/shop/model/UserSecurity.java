package com.griso.shop.model;

import com.griso.shop.dto.UserDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserSecurity implements UserDetails {

	private static final long serialVersionUID = 1L;

	private UserDto user;

	public UserSecurity(UserDto user) {
		this.setUser(user);
	}

	public UserSecurity() {
		this.setUser(new UserDto());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

		// Get permisions from db
		this.user.getPermissionList().forEach(p -> {
			authorities.add(new SimpleGrantedAuthority(p));
		});

		// Get roles from db
		this.user.getRoleList().forEach(r -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+r));
		});

		return authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
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
		return this.user.isActive();
	}

}
