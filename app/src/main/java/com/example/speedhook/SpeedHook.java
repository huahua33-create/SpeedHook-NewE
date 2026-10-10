package com.example.speedhook;

import android.util.Log;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedInterface.Hooker;
import io.github.libxposed.api.XposedInterface.HookHandle;

public class SpeedHook {
    public static void hookLocationSpeed(XposedModule module, ClassLoader classLoader) {
        try {
            Class<?> locationClass = classLoader.loadClass("android.location.Location");
            
            // 显式声明为 Executable 类型，防止编译器在重载方法中选错
            java.lang.reflect.Executable method = locationClass.getDeclaredMethod("getSpeed");
            
            // 显式声明 Hooker 类型
            XposedInterface.Hooker hooker = new XposedInterface.Hooker() {
                @Override
                public Object intercept(HookHandle chain) {
                    return SpeedConfigProvider.getSpeed();
                }
            };
            
            // 调用 hook 方法
            module.hook(method, hooker);
            
        } catch (Throwable t) {
            // 使用正确的 log 签名 (priority, tag, message)，避免编译错误
            module.log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
