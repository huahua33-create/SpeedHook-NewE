package com.example.speedhook;

import android.util.Log;
import java.lang.reflect.Executable;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface.Hooker;
import io.github.libxposed.api.XposedInterface.Chain;

public class MainModule extends XposedModule {
    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        // 只有目标应用才加载 Hook
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        
        try {
            // 直接使用 this.getClassLoader()，这是 XposedModule 自带的方法，100%不会报错
            Class<?> locationClass = this.getClassLoader().loadClass("android.location.Location");
            Executable method = locationClass.getDeclaredMethod("getSpeed");
            
            // 直接使用 this.hook()，这是 XposedModule 的原生方法，参数简单
            this.hook(method, new Hooker() {
                @Override
                public Object intercept(Chain chain) throws Throwable {
                    // 直接返回自定义速度，不调用原方法，完美覆盖
                    return SpeedConfigProvider.getSpeed();
                }
            });
            
            log(Log.INFO, "SpeedHook", "Loaded in " + param.getPackageName());
            
        } catch (Throwable t) {
            log(Log.ERROR, "SpeedHook", "Hook error: " + t.getMessage());
        }
    }
}
