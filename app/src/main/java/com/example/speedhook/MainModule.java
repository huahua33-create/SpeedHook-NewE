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
            
            Executable method = locationClass.getDeclaredMethod("getSpeed");

            XposedInterface.Hooker hooker = new XposedInterface.Hooker() {
                @Override
                public Object intercept(XposedInterface.Chain chain) throws Throwable {
                    return SpeedConfigProvider.getSpeed();
                }
            };

            // 新版 API：hook() 返回 HookBuilder，再调用 .intercept() 传入 Hooker
            this.hook(method).intercept(hooker);
            
            log(Log.INFO, "SpeedHook", "Loaded in " + param.getPackageName());
            
        } catch (Throwable t) {
            log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
