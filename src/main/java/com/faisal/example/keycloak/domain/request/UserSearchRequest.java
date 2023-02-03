package com.faisal.example.keycloak.domain.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserSearchRequest {
	private List<String> ids;
	private List<String> usernames;
	private List<String> emails;

	private String roleClientId;
}
