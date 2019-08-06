package com.xss.mobile.activity.databinding.senior;

/**
 * Created by xss on 2017/10/30.
 */

public interface Permissions {

    String P_ADD = "p_add";

    String P_DEL = "p_del";

    String P_QUERY = "p_query";

    String P_UPDATE = "p_update";

    void setPermissionEnable(boolean enable, boolean inVisibility);
}
