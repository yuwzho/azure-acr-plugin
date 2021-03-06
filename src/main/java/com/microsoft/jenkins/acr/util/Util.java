/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.jenkins.acr.util;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class Util {

    private Util() {
    }

    public static String toJson(Object o) {
        return new Gson().toJson(o);
    }

    public static <T> T retry(Callable<T> action, int time) throws Exception {
        try {
            return action.call();
        } catch (Exception e) {
            if (time == 0) {
                throw e;
            }
            Thread.sleep(Constants.SLEEP_IN_MS);
            return retry(action, --time);
        }
    }

    public static String[] toStringArray(List list) {
        List<String> result = toStringList(list);
        return result.toArray(new String[result.size()]);
    }

    public static List<String> toStringList(List list) {
        List<String> result = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return result;
        }

        for (Object o : list) {
            String s = StringUtils.trimToEmpty(o.toString());
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

    public static String getFileName(String path) {
        return new File(path).getName();
    }

    public static String normalizeFilename(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public static String concatPath(String root, String param) {
        if (Paths.get(param).isAbsolute()) {
            return normalizeFilename(param);
        }

        return normalizeFilename(Paths.get(root, param).toString());
    }

    /**
     * Verify whether the location is a legal git url or a local directory.
     *
     * @param location github url or local directory.
     * @return boolean
     */
    public static boolean verifyGitUrl(String location) {
        String source = StringUtils.trimToEmpty(location).toLowerCase();
        if (source.isEmpty()) {
            return true;
        }

        // SSH model for git is not supported
        return !source.startsWith(Constants.GIT_SSH_PREFIX);
    }
}

