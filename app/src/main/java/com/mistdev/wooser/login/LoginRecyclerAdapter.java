package com.mistdev.wooser.login;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mistdev.android_extensions.adapters.CursorRecyclerViewAdapter;
import com.mistdev.wooser.R;
import com.mistdev.wooser.data.models.User;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by mcastro on 04/03/17.
 */

class LoginRecyclerAdapter extends CursorRecyclerViewAdapter<LoginRecyclerAdapter.LoginViewHolder> {

    private LoginAdapterListener mListener;

    LoginRecyclerAdapter(Cursor cursor, LoginAdapterListener listener) {
        super(cursor);
        mListener = listener;
    }

    @Override
    public LoginViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_login_user, parent, false);
        return new LoginViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LoginViewHolder holder, Cursor cursor) {
        if(cursor == null) {
            return;
        }

        final User user = new User().fromCursor(cursor);
        if(user == null) {
            return;
        }

        holder.txtUserName.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUserClicked(user);
            }
        });

        if(user.getAvatarPath() == null) {
            return;
        }

        Uri uri = Uri.fromFile(new File(user.getAvatarPath()));
        Picasso.with(holder.itemView.getContext())
                .load(uri)
                .into(holder.imgUserAvatar);
    }

    /* VIEW HOLDER
     * ----------------------------------------------------------------------------------------------*/
    class LoginViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserAvatar;
        TextView txtUserName;

        LoginViewHolder(View itemView) {
            super(itemView);

            imgUserAvatar = (ImageView)itemView.findViewById(R.id.img_user_avatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txt_user_name);
        }
    }


    /* LISTENER
     * ----------------------------------------------------------------------------------------------*/
    interface LoginAdapterListener {
        void onUserClicked(User user);
    }
}
