package com.example.oleksandr.vk;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.vk.sdk.VKSdk;


public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        Fresco.initialize(this);
    }
}
