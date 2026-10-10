package com.example.speedhook;

import android.util.Log;
import java.lang.reflect.Executable;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedInterface.Chain;

public class SpeedHook {
    public static void hookLocationSpeed(XposedModule module, ClassLoader classLoader) {
        try {
            Class<?> locationClass = classLoader.loadClass("android.location.Location");
            // 显式声明为 Executable 类型
            Executable method = locationClass.getDeclaredMethod("getSpeed");

            // 注意：参数从 HookHandle 改成了 Chain
            XposedInterface.Hooker hooker = new XposedInterface.Hooker() {
                @Override
                public Object intercept(Chain chain) throws Throwable {
                    // 直接返回自定义速度，不调用原方法
                    return SpeedConfigProvider.getSpeed();
                }
            };

            // 加上优先级参数 50（这是最常见的新版 API 签名）
            module.hook(method, 50, hooker);

        } catch (Throwable t) {
            // 必须用 log(int, String, String) 格式
            module.log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
