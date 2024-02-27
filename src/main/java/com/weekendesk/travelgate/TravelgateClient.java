package com.weekendesk.travelgate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

@Repository
@Slf4j
public class TravelgateClient {

    private final HttpGraphQlClient graphQlClient;
    private int count = 0;

    public TravelgateClient() {
        WebClient client = WebClient.builder()
                .baseUrl("https://api.travelgatex.com")
                .defaultHeader("Authorization", "Apikey 8626cf56-e364-4fd1-4fe0-311e23ac6355")
                .build();
        graphQlClient = HttpGraphQlClient.builder(client).build();
    }

    public Flux<HotelX> getReactiveMultipleHotelSearches(List<DataRange> dateRanges) {
        AtomicLong requestCounter = new AtomicLong(0);
        Instant firstTime = Instant.now();
        return Flux.fromIterable(dateRanges)
                .flatMap(range -> {
                    requestCounter.incrementAndGet();
                    log.info("Request number {}", requestCounter.get());
                    return getReactiveHotelSearch(range);
                })
                .delayElements(Duration.ofMillis(15))
                .doFinally(signalType -> {
                    if (SignalType.ON_COMPLETE.equals(signalType)) {
                        log.info("Total number of requests: {}, time: {}",
                                requestCounter.get(),
                                Duration.between(firstTime, Instant.now()).toMillis());
                    }
                })
                .onErrorResume(error -> {
                    log.error("Error retreiving hotel search info: {}", error.getMessage());
                    return Flux.empty();
                });
    }

    public List<HotelX> getMultipleHotelSearches(List<DataRange> dateRanges) {
        Instant firstTime = Instant.now();
        return dateRanges.stream()
                .map(dataRange -> getHotelSearch(dataRange, firstTime))
                .toList();
    }

    private Mono<HotelX> getReactiveHotelSearch(DataRange dataRange) {
        return graphQlClient.documentName("search")
                .variable("checkIn", dataRange.checkin())
                .variable("checkOut", dataRange.checkout())
                .retrieve("hotelX")
                .toEntity(HotelX.class);
    }

    private HotelX getHotelSearch(DataRange dataRange, Instant firstTime) {
        count = count + 1;
        log.info("Number of request {}, time {}", count, Duration.between(firstTime, Instant.now()).toMillis());
        return graphQlClient.documentName("search")
                .variable("checkIn", dataRange.checkin())
                .variable("checkOut", dataRange.checkout())
                .retrieve("hotelX")
                .toEntity(HotelX.class)
                .block();
    }
}
