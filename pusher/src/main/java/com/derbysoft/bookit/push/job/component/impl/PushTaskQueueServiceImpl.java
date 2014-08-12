package com.derbysoft.bookit.push.job.component.impl;

import com.derbysoft.bookit.push.job.component.PushTaskQueueService;
import com.derbysoft.bookit.push.job.support.ChangesPushTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service("losRatePushQueueService")
public class PushTaskQueueServiceImpl implements PushTaskQueueService<ChangesPushTask> {
    private static final Logger LOGGER = Logger.getLogger(PushTaskQueueServiceImpl.class);

    private static final Map<String, LinkedBlockingQueue<ChangesPushTask>> TASK_KEY_MAP = new ConcurrentHashMap<String, LinkedBlockingQueue<ChangesPushTask>>();

    private static final Lock LOCK = new ReentrantLock();

    @Override
    public List<ChangesPushTask> getAllTasks() {
        List<ChangesPushTask> changesPushTasks = new ArrayList<ChangesPushTask>();
        for (LinkedBlockingQueue<ChangesPushTask> rateChangePushTasks : TASK_KEY_MAP.values()) {
            changesPushTasks.addAll(rateChangePushTasks);
        }
        return changesPushTasks;
    }

    @Override
    public void add(String hotel, List<String> keysList) {
        try {
            LOCK.lock();
            LinkedBlockingQueue<ChangesPushTask> changesPushTasks = TASK_KEY_MAP.get(hotel);
            if (changesPushTasks == null) {
                changesPushTasks = new LinkedBlockingQueue<ChangesPushTask>();
                TASK_KEY_MAP.put(hotel, changesPushTasks);
            }
            for (String key : keysList) {
                ChangesPushTask task = ChangesPushTask.build(hotel, key);
                if (changesPushTasks.contains(task)) {
                    continue;
                }
                changesPushTasks.add(task);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Set<String> getHotelCodes() {
        return TASK_KEY_MAP.keySet();
    }

    @Override
    public void remove(String hotelCode) {
        TASK_KEY_MAP.remove(hotelCode);
    }

    @Override
    public void remove(String hotelCode, String key) {
        LinkedBlockingQueue<ChangesPushTask> changesPushTasks = TASK_KEY_MAP.get(hotelCode);
        if (changesPushTasks == null) {
            return;
        }
        changesPushTasks.remove(ChangesPushTask.build(hotelCode, key));
    }

    @Override
    public String takeHotel() {
        if (TASK_KEY_MAP.isEmpty()) {
            return null;
        }
        return (String) TASK_KEY_MAP.keySet().toArray()[0];
    }

    @Override
    public Collection<ChangesPushTask> takeHotelTask(String hotelCode) {
        LOCK.lock();
        try {
            if (TASK_KEY_MAP.containsKey(hotelCode)) {
                return TASK_KEY_MAP.get(hotelCode);
            }
            return new ArrayList<ChangesPushTask>();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ArrayList<ChangesPushTask>();
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void cancel(String hotel) {
        Collection<ChangesPushTask> changesPushTasks = TASK_KEY_MAP.get(hotel);
        if (CollectionUtils.isEmpty(changesPushTasks)) {
            return;
        }
        for (ChangesPushTask task : changesPushTasks) {
            task.cancel();
        }
    }

    @Override
    public boolean updateCapacity(int capacity) {
        return false;
    }
}
