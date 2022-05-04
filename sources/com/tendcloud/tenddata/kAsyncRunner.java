package com.tendcloud.tenddata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class kAsyncRunner {
    private static final ExecutorService aExecutor = Executors.newSingleThreadExecutor();

    public static void aRun(Runnable runnable) {
        aExecutor.execute(runnable);
    }
}
