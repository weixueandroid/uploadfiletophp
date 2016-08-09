package com.zhangyp.higo.uploadfiletophp.core;

import com.zhangyp.higo.uploadfiletophp.api.Client;

/**
 * Created by zhangyipeng on 16/2/18.
 */
public class CancelQueue {


    public void cancel() {
        Client.stopAll();
    }

}
