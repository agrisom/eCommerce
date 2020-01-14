package com.griso.shop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class UserDto {

	private String id;
	private String username;
	private String password;
	private String name;
	private String surname;
	private Date birthday;
	private boolean active;
	private String roles;
	private String permissions;

	public UserDto() {}

	public UserDto(UserDto userToClone) {
		this.id = userToClone.getId();
		this.username = userToClone.getUsername();
		this.password = userToClone.getPassword();
		this.name = userToClone.getName();
		this.surname = userToClone.getSurname();
		this.birthday = userToClone.getBirthday();
		this.active = userToClone.isActive();
		this.roles = userToClone.getRoles();
		this.permissions = userToClone.getPermissions();
	}

	public List<String> getRoleList() {
		List<String> roleList = new ArrayList<>();
		if(!roles.trim().isEmpty()) {
			roleList.addAll(Arrays.asList(roles.split(";")));
		}

		return roleList;
	}

	public List<String> getPermissionList() {
		List<String> permissionList = new ArrayList<>();
		if(!permissions.trim().isEmpty()) {
			permissionList.addAll(Arrays.asList(permissions.split(";")));
		}

		return permissionList;
	}

	public void setRoleList(List<String> roleList) {
		this.roles = String.join(";", roleList);
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissions = String.join(";", permissionList);
	}

}