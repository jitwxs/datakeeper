package jit.wxs.demo.datakeeper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存刷新时间、过期时间 properties
 * @author jitwxs
 * @date 2019年08月18日 17:43
 */
@Data
@Component
@ConfigurationProperties(prefix = "data-keeper")
public class DataKeeperProperties {
    private boolean enabled;

    private DataKeeperProperties.Interval interval;

    private DataKeeperProperties.FailoverTime failoverTime;

    @Data
    public static class Interval {
        private long whiteUser;
    }

    @Data
    public static class FailoverTime {
        private long whiteUser;
    }
}
