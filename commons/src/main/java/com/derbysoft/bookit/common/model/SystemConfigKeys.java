package com.derbysoft.bookit.common.model;


public enum SystemConfigKeys {
    CHANGES_DATE_LENGTH("changes_date_length"),

    LOS_RATE_PULL_LEVEL("los_rate_pull_level"),
    HOW_MANY_CHECK_IN_IN_A_INVENTORY_PUSH_RQ("how_many_check_in_in_a_inventory_push_rq"),
    HOW_MANY_CHECK_IN_IN_A_RATE_PUSH_RQ("how_many_check_in_in_a_rate_push_rq"),

    SAVE_SUCCEED_LOS_RATE_PUSH_LOG("save_succeed_los_rate_push_log"),

    CONNECTION_TIMEOUT_SECONDS("connection_timeout_seconds"),
    SOCKET_TIMEOUT_SECONDS("socket_timeout_seconds"),
    USERNAME_PASSWORD("username_password"),

    LOS_RATE_PUSH_THREAD_COUNT("los_rate_push_thread_count"),
    LOS_RATE_PUSH_JOB("pushRateChangeJob"),
    LOS_RATE_PUSH_TASK_GENERATE_JOB("generateLOSRatePushTaskJob");

    private String key;

    private SystemConfigKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
