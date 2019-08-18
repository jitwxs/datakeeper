package jit.wxs.demo.datakeeper.bean;

import jit.wxs.demo.bean.User;
import lombok.Data;

/**
 * 白名单用户
 * @author jitwxs
 * @date 2019年08月18日 17:52
 */
@Data
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
