package com.numo.domain.wordbook;

public enum WordBookRole {
    view(true, false, false),
    edit(true, true, false),
    admin(true, true, true)
    ;

    WordBookRole(boolean canView, boolean canEdit, boolean canAll) {
        this.canView = canView;
        this.canEdit = canEdit;
        this.canAll = canAll;
    }

    private final boolean canView;
    private final boolean canEdit;
    private final boolean canAll;

    // 권한을 확인하는 함수들
    public boolean hasViewPermission() {
        return canView;
    }

    public boolean hasEditPermission() {
        return canEdit;
    }

    public boolean hasAdminPermission() {
        return canAll;
    }
}
