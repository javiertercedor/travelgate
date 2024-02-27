package com.weekendesk.travelgate;

import java.util.List;

public record HotelX (Search search){

    public record Search(String context,
            List<Warning> errors,
            List<Warning> warnings,
            List<Option> options) {

        public record Warning (String code,
                String type,
                String description) {
        }

        public record Option(String id,
                String hotelCode,
                String status,
                Price price) {

            public record Price (int net,
                    int gross) {

            }
        }
    }
}

