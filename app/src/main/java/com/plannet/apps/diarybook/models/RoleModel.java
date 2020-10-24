package com.plannet.apps.diarybook.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class RoleModel {

    transient int id;
    @SerializedName("name")
    @Expose
    String roleName;
    @SerializedName("id")
    @Expose
    int roleId;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("priority")
    @Expose
    int priority;
    @SerializedName("active")
    @Expose
    boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
