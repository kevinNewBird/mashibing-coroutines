package com.mashibing.coroutines.kilim;

import kilim.Pausable;
import kilim.Task;

/**
 * description  Actor <BR>
 * <p>
 * author: zhao.song
 * date: created in 8:33  2022/4/8
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class Actor extends Task {

    @Override
    public void execute() throws Exception, Pausable {
        System.out.println("hello");
        yield();
        System.out.println("你");
        yield();
        System.out.println("finish");
    }
}
