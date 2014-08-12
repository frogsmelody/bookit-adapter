package com.derbysoft.bookit.push.job.component;

import com.derbysoft.bookit.dto.OTAMessage;
import com.derbysoft.bookit.push.commons.models.NotifyRequestsPair;

import java.util.List;
import java.util.Map;

public interface NotifyRQPaginator<T extends OTAMessage> {
    List<NotifyRequestsPair<T>> paginate(Map<String, T> notifyRequestMap);
}
