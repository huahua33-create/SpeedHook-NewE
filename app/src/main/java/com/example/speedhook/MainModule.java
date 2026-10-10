package com.example.speedhook;

import android.util.Log;
import java.lang.reflect.Executable;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface;

public class MainModule extends XposedModule {
    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        // 只有目标应用才加载
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        
        try {
            Class<?> locationClass = Class.forName("android.location.Location");
            
            // 关键修复：类型明确声明为 Executable，不要用 Method
            Executable method = locationClass.getDeclaredMethod("getSpeed");

            // 显式声明 Hooker 类型
            XposedInterface.Hooker hooker = new XposedInterface.Hooker() {
                @Override
                public Object intercept(XposedInterface.Chain chain) throws Throwable {
                    return SpeedConfigProvider.getSpeed();
                }
            };

            // 传入 Executable 和 Hooker
            this.hook(method, hooker);
            
            log(Log.INFO, "SpeedHook", "Loaded in " + param.getPackageName());
            
        } catch (Throwable t) {
            log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
