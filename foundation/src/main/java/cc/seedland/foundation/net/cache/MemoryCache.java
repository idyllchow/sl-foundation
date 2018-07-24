

package cc.seedland.foundation.net.cache;

import java.lang.reflect.Type;

/**
 * <p>描述：内存缓存</p>
 */

public class MemoryCache extends BaseCache {
    @Override
    protected boolean doContainsKey(String key) {
        return false;
    }

    @Override
    protected boolean isExpiry(String key, long existTime) {
        return false;
    }

    @Override
    protected <T> T doLoad(Type type, String key) {
        return null;
    }

    @Override
    protected <T> boolean doSave(String key, T value) {
        return false;
    }

    @Override
    protected boolean doRemove(String key) {
        return false;
    }

    @Override
    protected boolean doClear() {
        return false;
    }
}
