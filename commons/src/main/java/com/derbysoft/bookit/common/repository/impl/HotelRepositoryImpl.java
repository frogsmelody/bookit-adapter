package com.derbysoft.bookit.common.repository.impl;

import com.derbysoft.bookit.common.repository.HotelRepository;
import com.derbysoft.ccs.core.MappingCache;
import com.derbysoft.synchronizer.common.ccs.Hotel;

import java.util.List;

public class HotelRepositoryImpl implements HotelRepository {
    private MappingCache<Hotel> hotelMappingCache;

    @Override
    public List<Hotel> loadAll() {
        return hotelMappingCache.listAll();
    }

    public void setHotelMappingCache(MappingCache<Hotel> hotelMappingCache) {
        this.hotelMappingCache = hotelMappingCache;
    }
}
