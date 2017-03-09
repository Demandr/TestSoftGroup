package com.example.oleksandr.vk;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.methods.VKApiMessages.*;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 07.03.2017.
 */

public class DialogFragment extends Fragment{
    private static final String TAG = "DialogFragment";
    private static final String ARG_USER_ID = "vk_user_id";


    private static int mUserID;

    private Button mBtnSend;
    private EditText mTextMessage;
    private JSONArray jsonArray;
    private List<DialogItem> mDialogItems = new ArrayList<>();

    public static Fragment newInstance (int userID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, userID);

        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserID = (int) getArguments().getSerializable(ARG_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        mBtnSend = (Button) v.findViewById(R.id.btn_dialog_send);
        mTextMessage = (EditText) v.findViewById(R.id.text_dialog_message);
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_dialog);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VKRequest vkRequest = new VKRequest("messages.send", VKParameters.from(
                        VKApiConst.USER_ID, mUserID, VKApiConst.MESSAGE, mTextMessage.getText()));
                vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        Toast toast = Toast.makeText(getActivity(), "Send", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        mDialogItems.clear();
                        mDialogItems = update_dialog();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDialogItems = update_dialog();
        recyclerView.setAdapter(new DialogAdapter(mDialogItems));
        recyclerView.getAdapter().notifyDataSetChanged();

        return v;
    }

    private class DialogHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView mImage;
        private TextView mText;

        public DialogHolder(View itemView) {
            super(itemView);

            mImage = (SimpleDraweeView) itemView.findViewById(R.id.image_friends);
            mText = (TextView) itemView.findViewById(R.id.text_message);
        }

        public void  bindDialog(DialogItem item){
            VKRequest requestTitle = VKApi.users().get(VKParameters.from("user_ids", item.getId(),
                    VKApiConst.FIELDS, "photo_50"));
            requestTitle.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKList<VKApiUser> user = ((VKList<VKApiUser>) response.parsedModel);
                    Uri mUri = Uri.parse(user.get(0).photo_50);
                    mImage.setImageURI(mUri);

                }
            });
            mText.setText(item.getText());
        }
    }

    private class DialogAdapter extends RecyclerView.Adapter<DialogHolder>{
        private List<DialogItem> mItemList;

        public DialogAdapter(List<DialogItem> itemList) {
            mItemList = itemList;
        }

        @Override
        public DialogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_dialogs, parent, false);
            return new DialogHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogHolder holder, int position) {
            DialogItem item = mItemList.get(position);
            holder.bindDialog(item);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }
    }

    public List<DialogItem> update_dialog(){
        final List<DialogItem> items = new ArrayList<>();

        final VKRequest mRequest = new VKRequest("messages.getHistory", VKParameters.from("user_id", mUserID,
                VKApiConst.FIELDS, "photo_50"));
        mRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = new JSONObject(response.json.toString());
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    jsonArray = jsonObject2.getJSONArray("items");
                    //Log.i(TAG, jsonArray.toString());
                }catch (JSONException je){
                    Log.e(TAG, "Failed to parse JSON" , je);
                }

                for (int i = 0; i < jsonArray.length(); i++){
                    try {
                        DialogItem item = new DialogItem();
                        item.setId(jsonArray.getJSONObject(i).getInt("from_id"));
                        item.setText(jsonArray.getJSONObject(i).getString("body"));
                        Log.i(TAG, item.getText() + "   " + item.getId());
                        items.add(item);
                    }catch (JSONException je){
                        Log.e(TAG, "Failed to parse JSON" , je);
                    }
                }

            }
        });
        return items;
    }
}
