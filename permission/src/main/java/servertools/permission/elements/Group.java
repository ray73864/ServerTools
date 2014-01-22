package servertools.permission.elements;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

public class Group {

    public final String groupName;
    private Set<String> members;
    private Set<String> allowedCommands;

    public Group(String groupName) {

        this.groupName = groupName;
        members = new HashSet<String>();
        allowedCommands = new HashSet<String>();
    }

    public void addMember(String username) {

        members.add(username);
    }

    public boolean removeUser(String username) {

        if (members.contains(username)) {
            members.remove(username);
            return true;
        }

        return false;
    }

    public boolean isMember(String username) {

        return members.contains(username);
    }

    public void addAllowedCommand(String commandName) {

        allowedCommands.add(commandName);
    }

    public boolean removeAllowedCommand(String commandName) {

        System.out.println("Trying to remove:" + commandName);

        for (String entry : allowedCommands) {
            System.out.println("Allowed Command:" + entry);
        }

        if (allowedCommands.contains(commandName)) {
            allowedCommands.remove(commandName);
            return true;
        }

        return false;
    }

    public Set<String> getMembers() {

        return ImmutableSet.copyOf(members);
    }

    public Set<String> getAllowedCommands() {

        return ImmutableSet.copyOf(allowedCommands);
    }
}
