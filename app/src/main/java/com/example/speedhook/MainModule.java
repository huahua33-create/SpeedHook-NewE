package com.example.speedhook;

import android.util.Log;
import java.lang.reflect.Executable;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedInterface.Chain;
import io.github.libxposed.api.XposedInterface.Hooker;

public class MainModule extends XposedModule {

    // 把 Hooker 显式定义成静态内部类，避免编译器推断错误
    public static class MySpeedHooker implements Hooker {
        @Override
        public Object intercept(Chain chain) throws Throwable {
            // 直接返回自定义速度，不执行原有方法，完美覆盖
            return SpeedConfigProvider.getSpeed();
        }
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        // 只有目标应用才加载
        if (!"com.example.targetapp".equals(param.getPackageName())) return;
        
        try {
            // 使用 Class.forName 直接加载系统类，不用 getClassLoader
            Class<?> locationClass = Class.forName("android.location.Location");
            Executable method = locationClass.getDeclaredMethod("getSpeed");
            
            // 传入我们刚才定义的静态内部类实例
            this.hook(method, new MySpeedHooker());
            
            log(Log.INFO, "SpeedHook", "Loaded successfully in " + param.getPackageName());
        } catch (Throwable t) {
            // 输出异常日志，防止静默失败
            log(Log.ERROR, "SpeedHook", "Error: " + t.getMessage());
        }
    }
}
