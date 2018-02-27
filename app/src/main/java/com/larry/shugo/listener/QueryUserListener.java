package com.larry.shugo.listener;

import com.larry.shugo.model.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

public abstract class QueryUserListener extends BmobListener<User> {

    public abstract void done(User s, BmobException e);
    @Override
    protected void postDone(User user, BmobException e) {
        done(user, e);
    }
}
