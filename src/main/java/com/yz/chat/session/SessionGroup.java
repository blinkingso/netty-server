package com.yz.chat.session;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class SessionGroup {
    private String groupId;
    private String groupName;
    private Set<Session> sessions;
}
