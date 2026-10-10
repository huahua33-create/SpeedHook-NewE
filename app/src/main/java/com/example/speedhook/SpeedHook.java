package com.example.speedhook;

import android.location.Location;
import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedInterface.HookHandle;
import io.github.libxposed.api.XposedInterface.Hooker;

public class SpeedHook {
    public static void hookLocationSpeed(ClassLoader classLoader) {
        try {
            Class<?> locationClass = classLoader.loadClass("android.location.Location");
            XposedInterface.getInstance().hook(
                locationClass.getDeclaredMethod("getSpeed"),
                new Hooker() {
                    @Override
                    public Object intercept(HookHandle chain) {
                        return SpeedConfigProvider.getSpeed();
                    }
                }
            );
            Class<?> lmClass = classLoader.loadClass("android.location.LocationManager");
            XposedInterface.getInstance().hook(
                lmClass.getDeclaredMethod("getLastKnownLocation", String.class),
                new Hooker() {
                    @Override
                    public Object intercept(HookHandle chain) {
                        Object result = chain.proceed();
                        if (result instanceof Location) {
                            ((Location) result).setSpeed(SpeedConfigProvider.getSpeed());
                        }
                        return result;
                    }
                }
            );
        } catch (Throwable t) {
            // ignore
        }
    }
}
