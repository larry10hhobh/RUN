package com.shu.runbang.listener;

public interface OnRecyclerViewClickListener {

    void onItemClick(int position);

    boolean onItemLongClick(int position);

    void onChildClick(int position,int childId);
}
