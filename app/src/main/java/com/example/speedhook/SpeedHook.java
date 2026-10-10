package com.example.speedhook;

import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface.Hooker;
import io.github.libxposed.api.XposedInterface.HookHandle;

public class SpeedHook {
    public static void hookLocationSpeed(XposedModule module, ClassLoader classLoader) {
        try {
            Class<?> locationClass = classLoader.loadClass("android.location.Location");
            
            // 直接使用 module 的 hook 方法
            module.hook(
                locationClass.getDeclaredMethod("getSpeed"),
                new Hooker() {
                    @Override
                    public Object intercept(HookHandle chain) {
                        // 直接返回自定义速度，不调用 chain.proceed()
                        // 这样原有方法就不会执行，完美篡改速度
                        return SpeedConfigProvider.getSpeed();
                    }
                }
            );
        } catch (Throwable t) {
            module.log("Hook error: " + t.getMessage());
        }
    }
}
