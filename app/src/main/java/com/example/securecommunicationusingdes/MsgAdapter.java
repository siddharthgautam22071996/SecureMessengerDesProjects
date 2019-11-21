package com.example.securecommunicationusingdes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MsgAdapter extends
        RecyclerView.Adapter<MsgAdapter.MyViewHolder> {

    public Context mContext;
    private ArrayList<ResponseMessage> dataItem;

    public MsgAdapter(Context mContext, ArrayList<ResponseMessage> dataItem) {
        this.dataItem = dataItem;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ResponseMessage c = dataItem.get(position);

        UserSessionManager userSessionManager = new UserSessionManager(mContext);
        String user = userSessionManager.getUserDetails().get("mob");
        TypeMsgActivity.flag=false;
        if (c.getSender().equalsIgnoreCase("" + user)) {

            holder.ll_send.setVisibility(View.VISIBLE);
            holder.ll_rec.setVisibility(View.GONE);

            if (c.getIsEncypted().equalsIgnoreCase("0")) {
                //TypeMsgActivity.typeMsgActivity.setSwitch(false);
                try {
                    holder.send.setText("" + Des._decrypt(c.getMsg(), c.getKey()) + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                //TypeMsgActivity.typeMsgActivity.setSwitch(true);
                holder.send.setText("" + c.getMsg() + " ");
            }
            holder.send_id.setText(c.getMsg_id());
        } else if (c.getReceiver().equalsIgnoreCase("" + user)) {
            holder.ll_rec.setVisibility(View.VISIBLE);
            holder.ll_send.setVisibility(View.GONE);
            if (c.getIsEncypted().equalsIgnoreCase("0")) {
                //TypeMsgActivity.typeMsgActivity.setSwitch(false);
                try {
                    holder.recive.setText("" + Des._decrypt(c.getMsg(), c.getKey()) + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //TypeMsgActivity.typeMsgActivity.setSwitch(true);
                holder.recive.setText("" + c.getMsg() + " ");
            }
            holder.rec_id.setText("" + c.getMsg_id() + " ");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c.getIsEncypted().equalsIgnoreCase("1")){
                    TypeMsgActivity.typeMsgActivity.dialog(c.getMsg(), 1, position);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_msg_view, parent, false);


        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView send, recive, send_id, rec_id;
        public LinearLayout ll_send, ll_rec;
        public Context mContext;


        public MyViewHolder(View view) {
            super(view);

            send = view.findViewById(R.id.send_msg);
            rec_id = view.findViewById(R.id.msg_id);
            send_id = view.findViewById(R.id.send_msg_id);

            recive = view.findViewById(R.id.rec_msg);
            ll_rec = view.findViewById(R.id.ll_rec);
            ll_send = view.findViewById(R.id.ll_send);




            /*code = (TextView) view.findViewById(R.id.code);
            name = (TextView) view.findViewById(R.id.vendor_name);
            contact = (TextView) view.findViewById(R.id.contact);
            tin = (TextView) view.findViewById(R.id.tinNo);*/
        }
    }


}