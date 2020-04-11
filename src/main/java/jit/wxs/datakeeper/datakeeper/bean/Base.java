package jit.wxs.datakeeper.datakeeper.bean;

/**
 * DataKeeper 缓存实体基类
 * @author jitwxs
 * @date 2019年08月18日 17:29
 */
public abstract class Base {
    private final long failoverTime;

    private final long time;

    public Base(long failoverTime) {
        this.failoverTime = failoverTime;
        this.time = System.currentTimeMillis();
    }

    public boolean expired() {
        return (System.currentTimeMillis() - this.time) > this.failoverTime;
    }
}
