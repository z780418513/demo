package com.hb.thread.context;

import java.util.Objects;
import java.util.UUID;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/20
 */
public class MdcIdContext {

    private static final ThreadLocal<String> TRACK_ID_HOLDER = new ThreadLocal<>();

    public static String getTrackId() {
        String uuid = TRACK_ID_HOLDER.get();
        if (Objects.isNull(uuid)) {
            uuid = UUID.randomUUID().toString().replace("-", "");
            TRACK_ID_HOLDER.set(uuid);
        }
        return uuid;
    }

    public static void remove(){
        TRACK_ID_HOLDER.remove();
    }
}
