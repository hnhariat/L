package com.sun.l.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.l.R;
import com.sun.l.models.DataFolder;

import java.util.ArrayList;

/**
 * Created by sunje on 2016-04-08.
 */
public class AdapterMakeGroup extends RecyclerView.Adapter {
    private ArrayList mList = new ArrayList();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View child = null;
        RecyclerView.ViewHolder holder = null;
        if (viewType == DataFolder.TYPE_NORMAL) {
            child = View.inflate(parent.getContext(), R.layout.item_group, null);
            holder = new ViewHolderGroupList(child);
        } else if (viewType == DataFolder.TYPE_NEW) {
            child = View.inflate(parent.getContext(), R.layout.item_group_add, null);
            holder = new ViewHolderGroupAdd(child);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerView.ViewHolder h = null;
        TextView txt = null;
        if (holder instanceof ViewHolderGroupList) {
            h = (ViewHolderGroupList) holder;
            txt = ((ViewHolderGroupList)h).txtFolder;
        } else if (holder instanceof ViewHolderGroupAdd) {
            h = (ViewHolderGroupAdd) holder;
            txt = ((ViewHolderGroupAdd)h).txtFolder;
        }
        DataFolder folder = (DataFolder) mList.get(position);
        txt.setText(folder.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.size() < 1) {
            return super.getItemViewType(position);
        }
        return ((DataFolder) mList.get(position)).getType();
    }

    public void setList(ArrayList list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}

class ViewHolderGroupList extends RecyclerView.ViewHolder {
    public TextView txtFolder = null;

    public ViewHolderGroupList(View itemView) {
        super(itemView);
        txtFolder = (TextView) itemView.findViewById(R.id.txt_folder);
    }
}

class ViewHolderGroupAdd extends RecyclerView.ViewHolder {
    public TextView txtFolder = null;

    public ViewHolderGroupAdd(View itemView) {
        super(itemView);
        txtFolder = (TextView) itemView.findViewById(R.id.txt_folder);
    }
}
