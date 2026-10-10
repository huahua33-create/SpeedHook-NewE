package com.example.speedhook;

import android.util.Log;
import io.github.libxposed.api.XposedModule;

public class MainModule extends XposedModule {
    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        SpeedHook.hookLocationSpeed(this, param.getClassLoader());
        // 修复 1：log 必须带上日志级别
        log(Log.INFO, "SpeedHook", "Loaded in " + param.getPackageName());
    }
}
