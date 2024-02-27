package com.weekendesk.travelgate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record DataRange (String checkin, String checkout){

    public static List<DataRange> getDateRangeForPriceAndAvail() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(startDate.getYear(), 12, 31);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<DataRange> dataRanges = new ArrayList<>();

        while (!startDate.isAfter(endDate)) {
            final String checkInFormatted = startDate.format(formatter);
            for (int i =1 ; i <= 21; i++) {
                final String checkOutFormatted = startDate.plusDays(i).format(formatter);
                dataRanges.add(new DataRange(checkInFormatted, checkOutFormatted));
            }
            startDate = startDate.plusDays(1);
        }

        return dataRanges;
    }

    public static List<DataRange> getDateRangeForCalendar() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<DataRange> dataRanges = new ArrayList<>();

        while (!startDate.isAfter(endDate)) {
            final String checkInFormatted = startDate.format(formatter);
            for (int i =1 ; i <= 2; i++) {
                final String checkOutFormatted = startDate.plusDays(i).format(formatter);
                dataRanges.add(new DataRange(checkInFormatted, checkOutFormatted));
            }
            startDate = startDate.plusDays(1);
        }

        return dataRanges;
    }
}
