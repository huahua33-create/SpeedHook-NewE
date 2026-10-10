package com.example.speedhook;

import android.util.Log;
import java.lang.reflect.Method;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface;

public class MainModule extends XposedModule {
    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        
        try {
            // 使用 Class.forName 加载系统类
            Class<?> locationClass = Class.forName("android.location.Location");
            // 使用反射获取方法
            Method method = locationClass.getDeclaredMethod("getSpeed");

            // 显式声明为 XposedInterface.Hooker 类型，避免编译器推断错误
            XposedInterface.Hooker hooker = new XposedInterface.Hooker() {
                @Override
                public Object intercept(XposedInterface.Chain chain) throws Throwable {
                    // 直接返回自定义速度
                    return SpeedConfigProvider.getSpeed();
                }
            };

            // 调用 hook 方法
            this.hook(method, hooker);
            
            // 新版 log 必须带日志级别和 Tag
            log(Log.INFO, "SpeedHook", "Loaded in " + param.getPackageName());
            
        } catch (Throwable t) {
            // 记录错误日志，防止静默失败
            log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
