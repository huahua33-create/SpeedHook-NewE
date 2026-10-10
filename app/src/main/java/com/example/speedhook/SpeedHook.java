package com.example.speedhook;

import android.util.Log;
import java.lang.reflect.Executable;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedInterface.Chain;
import io.github.libxposed.api.XposedInterface.Hooker;

public class SpeedHook {
    public static void hookLocationSpeed(XposedModule module, ClassLoader classLoader) {
        try {
            Class<?> locationClass = classLoader.loadClass("android.location.Location");
            Executable method = locationClass.getDeclaredMethod("getSpeed");

            Hooker hooker = new Hooker() {
                @Override
                public Object intercept(Chain chain) throws Throwable {
                    return SpeedConfigProvider.getSpeed();
                }
            };

            // 修复 2：去掉优先级 int 参数，直接传 method 和 hooker
            module.hook(method, hooker);

        } catch (Throwable t) {
            // 修复 3：log 必须带上日志级别
            module.log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
