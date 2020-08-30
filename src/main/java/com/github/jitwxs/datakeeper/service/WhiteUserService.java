package com.github.jitwxs.datakeeper.service;

import com.github.jitwxs.datakeeper.bean.User;

import java.util.List;

/**
 * @author jitwxs
 * @date 2019年08月18日 18:14
 */
public interface WhiteUserService {
    List<User> listWhiteUser();

    boolean isWhiteUser(long userId);

    User getById(long userId);
}
