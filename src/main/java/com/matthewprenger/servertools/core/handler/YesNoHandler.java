package com.matthewprenger.servertools.core.handler;

import com.matthewprenger.servertools.core.YesNoRequest;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class YesNoHandler {

    public static Map<String, Deque<YesNoRequest>> yesNoMap = new HashMap<String, Deque<YesNoRequest>>();

    public static void addYesNoRequest(String username, YesNoRequest request) {

        if (!yesNoMap.containsKey(username))
            yesNoMap.put(username, new LinkedList<YesNoRequest>());

        if (request != null)
            yesNoMap.get(username).add(request);
    }

    public static boolean acceptLastRequest(String username) {

        if (!yesNoMap.containsKey(username))
            return false;

        YesNoRequest request = yesNoMap.get(username).pollLast();

        if (request == null) return false;

        request.onYes();

        return true;
    }

    public static boolean declineLastRequest(String username) {

        if (!yesNoMap.containsKey(username))
            return false;

        YesNoRequest request = yesNoMap.get(username).pollLast();

        if (request == null) return false;

        request.onNo();

        return true;
    }

}
