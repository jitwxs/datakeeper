package com.github.jitwxs.datakeeper.datakeeper.cache;

import com.github.jitwxs.datakeeper.bean.User;
import com.github.jitwxs.datakeeper.datakeeper.bean.WhiteUserData;
import com.github.jitwxs.datakeeper.service.WhiteUserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 白名单用户缓存
 * @author jitwxs
 * @date 2019年08月18日 18:01
 */
@Slf4j
public class WhiteUserCache extends Base {
    public static final String NAME = "whiteUser cache";
    private long failoverTime;
    /**
     * 缓存存储
     */
    private final Map<Long, WhiteUserData> whiteUserCache;

    private WhiteUserService whiteUserService;

    public WhiteUserCache(WhiteUserService whiteUserService, long failoverTime) {
        super(NAME);
        this.whiteUserService = whiteUserService;
        this.failoverTime = failoverTime;
        this.whiteUserCache = new ConcurrentHashMap<>();
    }

    public User get(long userId) {
        WhiteUserData data = whiteUserCache.get(userId);
        if(data == null) {
            return null;
        }

        // 数据过期，手动刷新
        if(data.expired()) {
            if(whiteUserService.isWhiteUser(userId)) {
                User user = whiteUserService.getById(userId);
                setData(userId, user);

                log.warn("[DataKeeper] refresh {} delay, but get refresh, userId: {}", NAME, userId);
                return user;
            }
            return null;
        }
        return data.getData();
    }

    @Override
    void refresh() {
        List<User> whiteUser = whiteUserService.listWhiteUser();
        whiteUser.forEach(e -> setData(e.getUserId(), e));
    }

    private void setData(Long userId, User user) {
        whiteUserCache.put(userId, new WhiteUserData(user, failoverTime));
    }
}
