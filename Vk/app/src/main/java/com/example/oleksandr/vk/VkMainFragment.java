package com.example.oleksandr.vk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPostArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class VkMainFragment extends Fragment{
    private static final String TAG = "VkMainFragment";


    private TextView mFullName;
    private SimpleDraweeView mImageProfile;



    public static Fragment newInstance() {
        return new VkMainFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_wall, container, false);
        mFullName = (TextView) v.findViewById(R.id.userName);
        mImageProfile = (SimpleDraweeView) v.findViewById(R.id.imageView_profile);
        if(!VKSdk.isLoggedIn())
        {
            Fragment fLogin = LoginFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fLogin).commit();
        }else {
            final VKRequest request= VKApi.users().get(
                    VKParameters.from(VKApiConst.FIELDS, "photo_100,photo_100,online"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    VKList<VKApiUser> userMe = ((VKList<VKApiUser>) response.parsedModel);
                    Log.i(TAG, userMe.get(0).photo_100 + "");
                    Uri uri = Uri.parse(userMe.get(0).photo_100);
                    mImageProfile.setImageURI(uri);
                    mFullName.setText(userMe.get(0).first_name + " " + userMe.get(0).last_name);
                }
            });
            VKRequest reqWall=VKApi.wall().get(VKParameters.from("owner_id",58943615,"extended",1));
            reqWall.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKPostArray vkPostArray = (VKPostArray) response.parsedModel;

                    Log.i(TAG, vkPostArray.get(0).text + "");


                    RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.userWall);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new WallAdapter(vkPostArray));

                }

            });
        }

        return v;
    }

    private class WallHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView textNamePost;
        TextView textDatePost;
        TextView textPost;
        SimpleDraweeView imageWall;

        public WallHolder(View itemView) {
            super(itemView);

            cv=(CardView) itemView.findViewById(R.id.card_view_wall);
            textDatePost=(TextView)itemView.findViewById(R.id.textDatePost);
            textNamePost=(TextView)itemView.findViewById(R.id.textNamePost);
            textPost=(TextView)itemView.findViewById(R.id.textPost);
            imageWall=(SimpleDraweeView)itemView.findViewById(R.id.imageWall);
        }

        public void bindWall(VKApiPost vkApiPost){

            imageWall.setVisibility(View.VISIBLE);
            //Uri uri = Uri.parse(vkApiPost.);
            //imageWall.setImageURI(uri);
            //textNamePost.setText(vkApiPost.);
            textPost.setText(vkApiPost.text);
            Date time = new java.util.Date((long)vkApiPost.date*1000);
            textDatePost.setText(DateFormat.format("dd.MM.yyyy", time));

            int id = vkApiPost.from_id;

            VKRequest requestTitle = VKApi.users().get(VKParameters.from("user_ids", id,
                    VKApiConst.FIELDS, "photo_100, photo_50"));
            requestTitle.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKList<VKApiUser> user = ((VKList<VKApiUser>) response.parsedModel);
                    Uri uri = Uri.parse(user.get(0).photo_100);
                    imageWall.setImageURI(uri);
                    textNamePost.setText(user.get(0).first_name + " " + user.get(0).last_name);
                }
            });


        }
    }

    private class WallAdapter extends RecyclerView.Adapter<WallHolder>{
        VKPostArray posts;

        WallAdapter(VKPostArray post){
            this.posts = post;

        }

        @Override
        public WallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view =inflater.inflate(R.layout.item_wall,parent, false);
            return new WallHolder(view);
        }

        @Override
        public void onBindViewHolder(WallHolder holder, int position) {
            final VKApiPost pos = posts.get(position);
            holder.bindWall(pos);
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }

}
