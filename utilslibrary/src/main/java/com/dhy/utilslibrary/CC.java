package com.dhy.utilslibrary;

import android.app.Application;

public class CC {

    private static Application app;

    public static void setApplication(Application app) {
        CC.app = app;
    }

    public static Application getApplication() {
        return app;
    }
}
