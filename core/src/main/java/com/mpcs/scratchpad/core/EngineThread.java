package com.mpcs.scratchpad.core;

import java.util.UUID;

public class EngineThread extends Thread {

    private UUID uuid;

    public EngineThread(Runnable task, String name) {
        super(task, name);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void run() {
        Context.threadEngineUuid.set(uuid);
        super.run();
    }
}
