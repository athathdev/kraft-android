package com.spaceuptech.kraft.utility;

import java.util.UUID;

/**
 * Created by ubuntu on 20/9/17.
 */

public class Conversions {
    static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
    public static long getTimeFromUUID(UUID uuid) {
        return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
    }
}
