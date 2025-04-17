package com.numo.domain.wordbook;

public enum WordBookRole {
    none(false, false, false),
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
    boolean hasViewPermission() {
        return canView;
    }

    boolean hasEditPermission() {
        return canEdit;
    }

    boolean hasAdminPermission() {
        return canAll;
    }
}
