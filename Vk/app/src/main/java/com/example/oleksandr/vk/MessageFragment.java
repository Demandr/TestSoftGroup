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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;


public class MessageFragment extends Fragment {
    private static final String TAG = "MessageFragment";

    //public VKApiUser mainUser;

    public static Fragment newInstance(VKApiUser vkApiUser) {

        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_message, container, false);
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        VKRequest requestMessages = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT,"20"));
        requestMessages.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiGetDialogResponse dialogs = (VKApiGetDialogResponse) response.parsedModel;

                recyclerView.setAdapter(new MessageAdapter(dialogs));

            }
        });


        return v;
    }

    private class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private SimpleDraweeView mViewMessage;
        private TextView mTextMessage;
        private TextView mTextUser;
        private Uri mUri;
        private StringBuilder mFriendName = new StringBuilder();
        private int id;

        public MessageHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mViewMessage = (SimpleDraweeView) itemView.findViewById(R.id.image_message);
            mTextMessage = (TextView) itemView.findViewById(R.id.textMessages);
            mTextUser = (TextView) itemView.findViewById(R.id.textName);

        }

        public void bindMessage(VKApiMessage vkApiMessage){
            mTextMessage.setText(vkApiMessage.body);
            mTextUser.setText(vkApiMessage.title);

            id = vkApiMessage.user_id;

            VKRequest requestTitle = VKApi.users().get(VKParameters.from("user_ids", id,
                    VKApiConst.FIELDS, "photo_100, photo_50"));
            requestTitle.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKList<VKApiUser> user = ((VKList<VKApiUser>) response.parsedModel);
                    mUri = Uri.parse(user.get(0).photo_100);
                    mViewMessage.setImageURI(mUri);
                    mFriendName.append(user.get(0).first_name).append(" ").append(user.get(0).last_name);
                    mTextUser.setText(mFriendName.toString());
                }
            });
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "PRESSED");
            Fragment fDialog = DialogFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fDialog).commit();
        }
    }

    private class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{
        VKApiGetDialogResponse mDialogs;

        MessageAdapter(VKApiGetDialogResponse dialogs){
            mDialogs = dialogs;
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_message, parent, false);
            return new MessageHolder(v);
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
            VKApiMessage message = mDialogs.items.get(position).message;
            holder.bindMessage(message);
        }

        @Override
        public int getItemCount() {
            return mDialogs.items.size();
        }
    }

}
