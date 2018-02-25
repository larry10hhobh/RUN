package com.shu.runbang.listener;

import com.shu.runbang.model.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

public abstract class QueryUserListener extends BmobListener<User> {

    public abstract void done(User s, BmobException e);
    @Override
    protected void postDone(User user, BmobException e) {
        done(user, e);
    }
}
