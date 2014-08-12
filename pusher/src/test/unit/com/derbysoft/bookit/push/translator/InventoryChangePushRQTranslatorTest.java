package com.derbysoft.bookit.push.translator;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.dto.OTAHotelAvailNotifRQ;
import com.derbysoft.dswitch.dto.hotel.cds.LOSInventoryDTO;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import utils.XMLTestSupport;

import java.util.*;

public class InventoryChangePushRQTranslatorTest extends XMLTestSupport {
    InventoryChangePushRQTranslator translator = new InventoryChangePushRQTranslator();
    SystemConfigRepository systemConfigRepository;

    @Before
    public void setUp() throws Exception {
        systemConfigRepository = EasyMock.createMock(SystemConfigRepository.class);

        translator.setSystemConfigRepository(systemConfigRepository);
    }

    @Test
    public void testTranslate() throws Exception {
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(new SystemConfig("", "5"));
        EasyMock.replay(systemConfigRepository);
        ArrayList<LOSInventoryDTO> inventoryList = new ArrayList<LOSInventoryDTO>();
        inventoryList.add(createLOSInventoryDTO("2012-01-01", "RatePlan-A", "RoomType-B", "2,6,9,8,5,1,6,5,9"));
        inventoryList.add(createLOSInventoryDTO("2012-01-05", "RatePlan-A", "RoomType-B", "2,6,9,8,6,1,7,9,5"));
        Map<String, OTAHotelAvailNotifRQ> hotelAvailNotifRQMap = translator.translate(inventoryList, "Hotel-A");
        String actual = formatArrayToJson(hotelAvailNotifRQMap,"timeStamp");
        assertEquals(readFile("InventoryChangePushRQTranslator_output_01.json"), actual);
    }

    @Test
    public void testTranslate2() throws Exception {
        EasyMock.expect(systemConfigRepository.load(EasyMock.anyObject(String.class))).andReturn(new SystemConfig("", "10"));
        EasyMock.replay(systemConfigRepository);
        ArrayList<LOSInventoryDTO> inventoryList = new ArrayList<LOSInventoryDTO>();
        inventoryList.add(createLOSInventoryDTO("2012-01-01", "RatePlan-A", "RoomType-B", "2,6,9,8,5,1,6"));
        inventoryList.add(createLOSInventoryDTO("2012-01-05", "RatePlan-A", "RoomType-B", "2,6,9,8,6,1,8"));
        Map<String, OTAHotelAvailNotifRQ> hotelAvailNotifRQMap = translator.translate(inventoryList, "Hotel-A");
        String actual = formatArrayToJson(hotelAvailNotifRQMap,"timeStamp");
        assertEquals(readFile("InventoryChangePushRQTranslator_output_02.json"), actual);
    }

    private LOSInventoryDTO createLOSInventoryDTO(String checkIn, String ratePlan, String roomType, String fplos) {
        LOSInventoryDTO losInventoryDTO = new LOSInventoryDTO();
        losInventoryDTO.setCheckInDate(checkIn);
        losInventoryDTO.setRatePlanCode(ratePlan);
        losInventoryDTO.setRoomTypeCode(roomType);
        losInventoryDTO.setFplos(fplos);
        return losInventoryDTO;
    }
}
