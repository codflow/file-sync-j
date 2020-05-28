package ink.codflow.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class AbstractThreadPool {

    String taskDefineName;

    int corePoolSize;
    int maximumPoolSize;
    long keepAliveTime;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

    volatile ExecutorService executorService = null;

    public AbstractThreadPool(String name) {
        this.taskDefineName = name;
    }

    <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(callable);
    }

    Future<?> submit(Runnable run) {
        return executorService.submit(run);
    }

    ExecutorService getThreadPoolLazy() {
        if (executorService != null) {
            return executorService;
        } else {
            synchronized (executorService) {
                if (executorService != null) {
                    return executorService;
                } else {
                    executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                            TimeUnit.MILLISECONDS, workQueue, new CustomThreadFactory(taskDefineName, false));
                    return executorService;
                }
            }
        }
    }

    class CustomThreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);
        private final String prefix;
        private final boolean daemoThread;
        private final ThreadGroup threadGroup;

        public CustomThreadFactory(String prefix, boolean daemo) {
            this.prefix = prefix.length()>0 ? prefix + "-thread-" : "";
            daemoThread = daemo;
            SecurityManager s = System.getSecurityManager();
            threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {

            String name = prefix + mThreadNum.getAndIncrement();
            Thread ret = new Thread(threadGroup, r, name, 0);
            ret.setDaemon(daemoThread);
            return ret;
        }

    }

}
