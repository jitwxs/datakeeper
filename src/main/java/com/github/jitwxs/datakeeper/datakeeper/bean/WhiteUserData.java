package com.github.jitwxs.datakeeper.datakeeper.bean;

import com.github.jitwxs.datakeeper.bean.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 白名单用户
 * @author jitwxs
 * @date 2019年08月18日 17:52
 */
@Getter
@Setter
public class WhiteUserData extends Base {
    /**
     * 白名单用户
     */
    private final User data;

    public WhiteUserData(User data, long failoverTime) {
        super(failoverTime);
        this.data = data;
    }
}
