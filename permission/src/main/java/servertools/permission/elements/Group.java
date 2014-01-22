package servertools.permission.elements;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

/*
 * Copyright 2014 matthewprenger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
