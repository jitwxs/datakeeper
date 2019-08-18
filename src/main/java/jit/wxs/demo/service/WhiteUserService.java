package jit.wxs.demo.service;

import jit.wxs.demo.bean.User;

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
