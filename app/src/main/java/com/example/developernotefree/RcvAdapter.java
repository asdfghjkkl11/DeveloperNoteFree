package com.example.developernotefree;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
/*
 * rcvadapter. adpater of recyclerview.
 * show memo lists.
 * handle data in realm
 */
public class RcvAdapter extends RecyclerView.Adapter<RcvAdapter.ViewHolder> {

    private Activity activity;
    private List<Memo> dataList;
    private Realm realm;

    public RcvAdapter(Activity activity, List<Memo> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.mContextTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(activity.getApplicationContext(),AddActivity.class);
                    Memo m=dataList.get(getAdapterPosition());
                    intent.putExtra("ID",m.getID());
                    intent.putExtra("title",m.getTitle());
                    intent.putExtra("text",m.getText());
                    activity.startActivityForResult(intent,1);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    removeMemo(dataList.get(getAdapterPosition()).getID());
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Memo data = dataList.get(position);
        holder.tvName.setText(data.getTitle());
    }

    private void removeItemView(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    // 데이터 삭제
    private void removeMemo(String text) {
        realm = Realm.getDefaultInstance();
        final RealmResults<Memo> results = realm.where(Memo.class).equalTo("ID",text).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFromRealm(0);
            }
        });
    }
}