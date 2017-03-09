package com.example.oleksandr.vk;

//import android.app.Fragment;
//import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

public abstract class SingleFragmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    protected abstract Fragment createFragment();
    private FragmentManager fm;
    private SimpleDraweeView mView;
    private TextView mTextView;
    VKApiUser mMainUser;


    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKSdk.initialize(this);
        setContentView(getLayoutResId());
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!VKSdk.isLoggedIn())
        {

        }else {
            final VKRequest request = VKApi.users().get(
                    VKParameters.from(VKApiConst.FIELDS, "photo_100,photo_100,online"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    VKList<VKApiUser> userMe = ((VKList<VKApiUser>) response.parsedModel);
                    mMainUser = userMe.get(0);
                    Uri uri = Uri.parse(userMe.get(0).photo_100);
                    View header = navigationView.getHeaderView(0);
                    mView = (SimpleDraweeView) header.findViewById(R.id.imageView_icon);
                    mView.setImageURI(uri);
                    mTextView = (TextView)  header.findViewById(R.id.full_name);
                    mTextView.setText(userMe.get(0).first_name + " " + userMe.get(0).last_name);
                }
            });
        }

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_exit:
                VKSdk.logout();
                Fragment fLogin = LoginFragment.newInstance();
                fm.beginTransaction().replace(R.id.fragment_container, fLogin);
                break;
            case R.id.nav_messages:
                Fragment fMessage = MessageFragment.newInstance(mMainUser);
                fm.beginTransaction().replace(R.id.fragment_container, fMessage).commit();
                break;
            case R.id.nav_friends:
                Fragment fFriend = FriendsFragment.newInstance();
                fm.beginTransaction().replace(R.id.fragment_container, fFriend).commit();
                break;
            case  R.id.nav_main:
                Fragment fMain = VkMainFragment.newInstance();
                fm.beginTransaction().replace(R.id.fragment_container, fMain).commit();
            default:

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
