package com.larry.shugo.listener;

public interface OnRecyclerViewClickListener {

    void onItemClick(int position);

    boolean onItemLongClick(int position);

    void onChildClick(int position,int childId);
}
