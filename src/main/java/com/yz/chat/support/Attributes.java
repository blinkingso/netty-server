package com.yz.chat.support;

import com.yz.chat.session.Group;
import com.yz.chat.session.Session;
import io.netty.util.AttributeKey;

import java.util.Set;

public interface Attributes {
    // Channel的登录状态
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    // Channel的Session信息（用户登录信息）
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
    // Channel的Group列表（当前Channel加入或创建的群组）
    AttributeKey<Set<Group>> GROUP = AttributeKey.newInstance("group");
}
