package com.hypernym.evaconnect.listeners;

import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface InvitedConnectionListener {
    void invitedConnections(List<User> invitedConnections);

}
