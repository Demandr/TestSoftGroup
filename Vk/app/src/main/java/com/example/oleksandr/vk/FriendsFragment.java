package com.example.oleksandr.vk;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;


public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    private VKUsersArray mList;



    public static Fragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        final VKRequest request= VKApi.friends().get(VKParameters.from("order", "hints",
                VKApiConst.FIELDS,"first_name,last_name,photo_100"));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                //VKUsersArray usersArray = (VKUsersArray) response.parsedModel;
                 mList = (VKUsersArray) response.parsedModel;

            }
        });
        recyclerView.setAdapter(new FriendsAdapter(mList));
        return v;
    }



    private class FriendsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private SimpleDraweeView mImageFriends;
        private TextView mNameFriends;

        public FriendsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mImageFriends = (SimpleDraweeView) itemView.findViewById(R.id.image_friends);
            mNameFriends = (TextView) itemView.findViewById(R.id.name_friends);
        }

        public void bindFriend(VKApiUserFull user){
            mNameFriends.setText(user.first_name + " " + user.last_name);
            Uri uri = Uri.parse(user.photo_100);
            mImageFriends.setImageURI(uri);
            Log.i(TAG, user.photo_100);

        }


        @Override
        public void onClick(View view) {

        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsHolder> {
        private VKList mVKList;

        public FriendsAdapter(VKList list){
            mVKList = list;
        }

        @Override
        public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater  = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_friends, parent, false);
            return new FriendsHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendsHolder holder, int position) {
            VKApiUserFull userFull = (VKApiUserFull) mVKList.get(position);
            holder.bindFriend(userFull);
        }

        @Override
        public int getItemCount() {
            return mVKList.size();
        }
    }

}
