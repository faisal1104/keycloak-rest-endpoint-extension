package com.faisal.example.keycloak.domain.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CustomUserRepresentation {
	private String id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private boolean isEnabled;
	private List<RoleRepresentation> clientLabelRoles;
}