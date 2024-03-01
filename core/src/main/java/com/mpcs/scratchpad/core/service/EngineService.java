package com.mpcs.scratchpad.core.service;

import com.mpcs.scratchpad.core.Context;

public interface EngineService {
    void init(Context context);
    void start();
    void stop();

    ServicePriority getPriority();

}
