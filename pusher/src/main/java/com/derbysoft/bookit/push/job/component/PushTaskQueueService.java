package com.derbysoft.bookit.push.job.component;

import com.derbysoft.bookit.push.job.support.PushTask;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PushTaskQueueService<T extends PushTask> {

    String takeHotel();

    Collection<T> takeHotelTask(String hotelCode);

    void cancel(String hotel);

    void add(String hotel, List<String> keysList);

    boolean updateCapacity(int capacity);

    List<T> getAllTasks();

    Set<String> getHotelCodes();

    void remove(String hotelCode);

    void remove(String hotelCode, String key);
}
