package com.github.jitwxs.datakeeper.service.impl;

import com.github.jitwxs.datakeeper.service.WhiteUserService;
import com.github.jitwxs.datakeeper.bean.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * @author jitwxs
 * @date 2019年08月18日 18:15
 */
@Service
public class WhiteUserServiceImpl implements WhiteUserService {
    @Override
    public List<User> listWhiteUser() {
        List<User> whiteUsers = new ArrayList<>();
        // 模拟读取白名单用户
        LongStream.range(1, 100).forEach(e -> {
            whiteUsers.add(new User(e, "user" + e));
        });

        return whiteUsers;
    }

    @Override
    public boolean isWhiteUser(long userId) {
        // 模拟判断是否是白名单用户
        return true;
    }

    @Override
    public User getById(long userId) {
        return new User(userId, "user" + userId);
    }
}
