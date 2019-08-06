package com.xss.mobile.activity.databinding.senior;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xss on 2017/10/30.
 */

public class PermissionCenter {

    public static Set<String> permissions;

    public static void setPermissions(List<String> list) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.clear();
        permissions.addAll(list);
    }

    public static boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
