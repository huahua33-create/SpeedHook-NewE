package com.example.speedhook;

import io.github.libxposed.api.XposedModule;

public class MainModule extends XposedModule {
    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        SpeedHook.hookLocationSpeed(param.getClassLoader());
        log("SpeedHook loaded in " + param.getPackageName());
    }
}
