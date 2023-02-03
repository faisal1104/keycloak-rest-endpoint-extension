package com.faisal.example.keycloak.resource;

import com.faisal.example.keycloak.domain.request.UserSearchRequest;
import com.faisal.example.keycloak.domain.response.CustomUserRepresentation;
import com.faisal.example.keycloak.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.keycloak.models.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MyResourceProvider implements RealmResourceProvider {

	@Context
	private final KeycloakSession session;

	@Override
	public Object getResource() {
		return this;
	}

	@Override
	public void close() {
	}

	@POST
	@Path("get-users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<CustomUserRepresentation> getUsersByIdList(UserSearchRequest userSearchRequest) {
		log.info("UserSearchRequest : {}", userSearchRequest);
		if (Objects.isNull(userSearchRequest)) {
			log.error("UserSearchRequest is null");
			return new ArrayList<>();
		}
		AuthService.checkAuth(session);

		RealmModel realmModel = session.getContext().getRealm();
		UserProvider userProvider = session.users();
		ClientModel clientModel = null;

		if (StringUtils.isNotBlank(userSearchRequest.getRoleClientId())) {
			clientModel = session.clients().getClientByClientId(realmModel, userSearchRequest.getRoleClientId());
		}

		List<CustomUserRepresentation> userRepresentations;
		if (CollectionUtils.isNotEmpty(userSearchRequest.getIds())) {
			userRepresentations = this.getUsersByIds(userSearchRequest.getIds(), userProvider, realmModel, clientModel);
		} else if (CollectionUtils.isNotEmpty(userSearchRequest.getUsernames())) {
			userRepresentations = this.getUsersByUserNames(userSearchRequest.getUsernames(), userProvider, realmModel,
					clientModel);
		} else if (CollectionUtils.isNotEmpty(userSearchRequest.getEmails())) {
			userRepresentations = this.getUsersByEmails(userSearchRequest.getEmails(), userProvider, realmModel,
					clientModel);
		} else
			userRepresentations = new ArrayList<>();

		return userRepresentations;
	}

	private List<CustomUserRepresentation> getUsersByIds(List<String> userIDs, UserProvider userProvider,
			RealmModel realmModel, ClientModel clientModel) {
		List<CustomUserRepresentation> userRepresentations = new ArrayList<>();
		for (String userID : userIDs) {
			try {
				var userModel = userProvider.getUserById(realmModel, userID);
				userRepresentations.add(this.createUserResponse(userModel, clientModel));
			} catch (Exception e) {
				log.error("Failed to retrieve user information with userID: {}", userID);
				e.printStackTrace();
			}
		}
		return userRepresentations;
	}

	private List<CustomUserRepresentation> getUsersByUserNames(List<String> userNames, UserProvider userProvider,
			RealmModel realmModel, ClientModel clientModel) {
		List<CustomUserRepresentation> userRepresentations = new ArrayList<>();
		for (String userName : userNames) {
			try {
				var userModel = userProvider.getUserByUsername(realmModel, userName);
				userRepresentations.add(this.createUserResponse(userModel, clientModel));
			} catch (Exception e) {
				log.error("Failed to retrieve user information with userName: {}", userName);
				e.printStackTrace();
			}
		}
		return userRepresentations;
	}

	private List<CustomUserRepresentation> getUsersByEmails(List<String> emails, UserProvider userProvider,
			RealmModel realmModel, ClientModel clientModel) {
		List<CustomUserRepresentation> userRepresentations = new ArrayList<>();
		for (String email : emails) {
			try {
				var userModel = userProvider.getUserByEmail(realmModel, email);
				userRepresentations.add(this.createUserResponse(userModel, clientModel));
			} catch (Exception e) {
				log.error("Failed to retrieve user information with email: {}", email);
				e.printStackTrace();
			}
		}
		return userRepresentations;
	}

	private CustomUserRepresentation createUserResponse(UserModel userModel, ClientModel clientModel) {
		var userRepresentation = new CustomUserRepresentation();
		var roleRepresentations = new ArrayList<RoleRepresentation>();

		List<RoleModel> roleModels = new ArrayList<>();
		if (Objects.nonNull(clientModel)) {
			roleModels = userModel.getClientRoleMappingsStream(clientModel).collect(Collectors.toList());
		}

		for (RoleModel roleModel : roleModels) {
			RoleRepresentation roleRepresentation = new RoleRepresentation();
			roleRepresentation.setName(roleModel.getName());
			roleRepresentation.setClientRole(roleModel.isClientRole());
			roleRepresentation.setAttributes(roleModel.getAttributes());
			roleRepresentation.setComposite(roleModel.isComposite());
			roleRepresentation.setId(roleModel.getId());
			roleRepresentation.setDescription(roleModel.getDescription());

			roleRepresentations.add(roleRepresentation);
		}

		return userRepresentation.setFirstName(userModel.getFirstName()).setLastName(userModel.getLastName())
				.setEmail(userModel.getEmail()).setUsername(userModel.getUsername()).setEnabled(userModel.isEnabled())
				.setId(userModel.getId()).setClientLabelRoles(roleRepresentations);
	}
}
