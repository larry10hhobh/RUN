package com.shu.shugo.listener;

import com.shu.shugo.model.bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

public abstract class QueryUserListener extends BmobListener<User> {

    public abstract void done(User s, BmobException e);
    @Override
    protected void postDone(User user, BmobException e) {
        done(user, e);
    }
}
