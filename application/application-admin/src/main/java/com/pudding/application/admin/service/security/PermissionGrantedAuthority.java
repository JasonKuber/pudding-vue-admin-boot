package com.pudding.application.admin.service.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;


public class PermissionGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Getter
    private Boolean isAdmin;

    private final String permissionCode;

    public PermissionGrantedAuthority(Boolean isAdmin) {
        this.permissionCode = "";
        this.isAdmin = isAdmin;
    }

    public PermissionGrantedAuthority(String permissionCode) {
        Assert.hasText(permissionCode, "A granted authority textual representation is required");
        this.permissionCode = permissionCode;
        this.isAdmin = false;
    }

    public PermissionGrantedAuthority(Boolean isAdmin,String permissionCode) {
        Assert.hasText(permissionCode, "A granted authority textual representation is required");
        this.permissionCode = permissionCode;
        this.isAdmin = isAdmin;
    }

    @Override
    public String getAuthority() {
        return this.permissionCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PermissionGrantedAuthority) {
            return this.permissionCode.equals(((PermissionGrantedAuthority) obj).permissionCode);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.permissionCode.hashCode();
    }

    @Override
    public String toString() {
        return this.permissionCode;
    }
}
