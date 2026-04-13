package org.project.sohwagi.application.cmd;

public record GetOrCreateUserCommand(
    String userName,
    String email,
    String oauthProvider
) {

}
