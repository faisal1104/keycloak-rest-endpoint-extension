# keycloak-rest-api-extensions

This module implements extensions to keycloak's REST API. Currently implemented functions are:

    * get users by list of id/username/email

The endpoints are available in every realm:

    /auth/realms/{realm-name}/additional-user-resource/get-users [POST]

Currently works under keycloak 15.0.2

## Pre-requisite

Requires java 11 and keycloak 15.0.2

## How to use this module
1. First create jar with command - `./gradelw clen build`
2. Copy the jar to the `keycloak/standalone/deployments/` folder and restart KeyCloak server

### Reference Documentation

* [What is KeyCloak](https://www.keycloak.org/)
* [Discussion about KC api extension](https://keycloak.discourse.group/t/best-way-to-extend-administrative-api/4468)
* [GitHub example -1](https://github.com/zonaut/keycloak-extensions/tree/master/spi-resource)
* [GitHub example -2](https://github.com/cloudtrust/keycloak-rest-api-extensions)
* [Youtube demo](https://www.youtube.com/watch?v=eZYGLuUrUp4)