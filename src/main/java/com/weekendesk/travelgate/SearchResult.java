package com.weekendesk.travelgate;

import com.weekendesk.travelgate.HotelX.Search.Option;
import org.springframework.data.annotation.Id;

public record SearchResult(@Id Long id,
        String searchId,
        String hotelCode,
        int price,
        String status) {

    public static SearchResult from(Option hotelOption) {
        return new SearchResult(null,
                hotelOption.id(),
                hotelOption.hotelCode(),
                hotelOption.price().net(),
                hotelOption.status());
    }
}
