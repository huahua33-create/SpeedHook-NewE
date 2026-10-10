package com.example.speedhook;

import io.github.libxposed.api.XposedModule;

public class MainModule extends XposedModule {
    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        // 将 this (即 XposedModule 实例) 传进去
        SpeedHook.hookLocationSpeed(this, param.getClassLoader());
        log("SpeedHook loaded in " + param.getPackageName());
    }
}
