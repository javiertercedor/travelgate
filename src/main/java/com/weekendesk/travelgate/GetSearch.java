package com.weekendesk.travelgate;

import static java.util.Objects.nonNull;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class GetSearch {

    private final TravelgateClient travelgateClient;
    private final SearchResultRespository respository;

    public void getReactiveSearch() {
        travelgateClient.getReactiveMultipleHotelSearches(DataRange.getDateRangeForPriceAndAvail())
                .doOnNext(this::handleOptions)
                .subscribe();
    }

    public void getReactiveSearchForCalendar() {
        travelgateClient.getReactiveMultipleHotelSearches(DataRange.getDateRangeForCalendar())
                .doOnNext(this::handleOptions)
                .subscribe();
    }

    public void getSearch() {
        travelgateClient.getMultipleHotelSearches(DataRange.getDateRangeForPriceAndAvail())
                .forEach(this::handleOptions);
    }

    private void handleOptions(HotelX hotelX) {
        if (nonNull(hotelX.search().options())) {
            final List<SearchResult> searchResults = hotelX.search().options().stream()
                    .map(SearchResult::from)
                    .toList();

            log.info("Search results list saved: {}", hotelX.search().options().size());
            respository.saveAll(searchResults);
        }
    }
}
