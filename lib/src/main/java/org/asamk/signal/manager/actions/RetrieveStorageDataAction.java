package org.asamk.signal.manager.actions;

import org.asamk.signal.manager.helper.Context;

public class RetrieveStorageDataAction implements HandleAction {

    private static final RetrieveStorageDataAction INSTANCE = new RetrieveStorageDataAction();

    private RetrieveStorageDataAction() {
    }

    public static RetrieveStorageDataAction create() {
        return INSTANCE;
    }

    @Override
    public void execute(Context context) throws Throwable {
        if (context.getAccount().getStorageKey() != null) {
            context.getStorageHelper().readDataFromStorage();
        } else if (!context.getAccount().isPrimaryDevice()) {
            context.getSyncHelper().requestSyncKeys();
        }
    }
}
