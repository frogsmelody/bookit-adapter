package com.derbysoft.bookit.common.commons.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.Executor;

public class DynamicThreadPoolExecutor implements Executor {
    private static Logger logger = LoggerFactory.getLogger(DynamicThreadPoolExecutor.class);
    private final Queue queue;
    private Thread[] threads;

    public DynamicThreadPoolExecutor(Queue queue, int threadCount) {
        this.queue = queue;
        threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            Worker worker = new Worker(i);
            threads[i] = worker;
        }
        checkStart();
    }

    private void checkStart() {
        for (Thread thread : threads) {
            if (thread.getState() == Thread.State.NEW) {
                thread.start();
            }
        }
    }

    public void updateThreadCount(int newThreadCount) {
        synchronized (queue) {
            if (newThreadCount < 1) {
                throw new IllegalArgumentException(String.format("Invalid thread count %d,Thread count must be great than 1", newThreadCount));
            }
            if (threads.length == newThreadCount) {
                return;
            }
            Thread[] newThreads = new Thread[newThreadCount];
            if (newThreadCount > threads.length) {
                System.arraycopy(threads, 0, newThreads, 0, threads.length);
                for (int index = threads.length; index < newThreadCount; index++) {
                    Worker worker = new Worker(index);
                    newThreads[index] = worker;
                    worker.start();
                }
            } else {
                System.arraycopy(threads, 0, newThreads, 0, newThreadCount);
                for (int exceededIndex = newThreads.length; exceededIndex < threads.length; exceededIndex++) {
                    Worker thread = (Worker) threads[exceededIndex];
                    thread.stopRunning();
                }
            }
            threads = newThreads;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    private final class Worker extends Thread {
        private boolean running = true;
        private int index;

        private Worker(int index) {
            this.index = index;
        }

        public void run() {
            while (running) {
                Runnable task = null;
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                    } else {
                        if (index < threads.length) {
                            task = (Runnable) queue.poll();
                        }
                    }
                }
                if (task != null) {
                    try {
                        task.run();
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
        }

        public void stopRunning() {
            running = false;
        }
    }

    public Thread[] getThreads() {
        return threads;
    }

    public int activeThreads() {
        int active = 0;
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                active++;
            }
        }
        return active;
    }

    public int remainingTask() {
        return queue.size();
    }

    public void shutdown() {
        synchronized (queue) {
            queue.clear();
            queue.notifyAll();
        }
    }

    public int getThreadCount() {
        return threads.length;
    }
}

