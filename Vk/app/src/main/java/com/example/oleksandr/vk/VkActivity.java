package com.example.oleksandr.vk;

import android.support.v4.app.Fragment;

public class VkActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return VkMainFragment.newInstance();
    }


}
