package com.faisal.example.keycloak.service;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.NotAuthorizedException;
import java.util.Objects;

public class AuthService {
	public static void checkAuth(KeycloakSession session) {
		AuthenticationManager.AuthResult auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
		if (Objects.isNull(auth)) {
			throw new NotAuthorizedException("Bearer");
		}
	}
}
