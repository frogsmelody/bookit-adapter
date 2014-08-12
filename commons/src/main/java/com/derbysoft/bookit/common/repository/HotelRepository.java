package com.derbysoft.bookit.common.repository;

import com.derbysoft.synchronizer.common.ccs.Hotel;

import java.util.List;

public interface HotelRepository {
    List<Hotel> loadAll();
}
