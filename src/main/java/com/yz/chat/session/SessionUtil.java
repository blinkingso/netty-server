package com.yz.chat.session;

import com.yz.chat.support.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.Attribute;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    // Session Mapping userId -> Channel
    // 用户登录cache
    private static final Map<String, Channel> sessionMap = new ConcurrentHashMap<>();

    // Group Mapping: groupId -> ChannelGroup
    // 群组cache
    private static final Map<String, ChannelGroup> groupsMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        sessionMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void bindChannelGroup(Session session, Group group, ChannelGroup channelGroup) {
        Channel channel = sessionMap.get(session.getUserId());
        if (channel != null) {
            Attribute<Set<Group>> attr = channel.attr(Attributes.GROUP);
            Set<Group> groups = attr.get();
            if (groups == null) {
                groups = new HashSet<>();
            }
            groups.add(group);
            attr.set(groups);

            groupsMap.put(group.getGroupId(), channelGroup);
        }
    }

    public static void unbindChannelGroup(Channel channel) {
        // remove channel group from channel
        Attribute<Set<Group>> attr = channel.attr(Attributes.GROUP);
        Set<Group> groups = attr.get();
        if (groups != null) {
            for (Group group : groups) {
                groupsMap.computeIfPresent(group.getGroupId(), (_key, c) -> {
                    c.remove(channel);
                    return c;
                });
            }
            attr.set(null);
            // 删除没有用户的群组
            groupsMap.entrySet().iterator().forEachRemaining(entry -> {
                if (entry.getValue() != null && entry.getValue().isEmpty()) {
                    groupsMap.remove(entry.getKey());
                }
            });
        }
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            sessionMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
            unbindChannelGroup(channel);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Set<Group> getGroups(Channel channel) {
        return channel.attr(Attributes.GROUP).get();
    }

    public static Channel getChannel(String userId) {
        return sessionMap.get(userId);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groupsMap.get(groupId);
    }
}
