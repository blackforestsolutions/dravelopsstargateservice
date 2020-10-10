package de.blackforestsolutions.dravelopsstargateservice.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.Locale;

public class ApiTokenObjectMothers {

    public static ApiToken getRequestToken() {
        return new ApiToken.ApiTokenBuilder()
                .setOptimize(Optimization.QUICK)
                .setIsArrivalDateTime(false)
                .setDateTime(ZonedDateTime.parse("2020-09-30T13:00:00+02:00"))
                .setDepartureCoordinate(new Point(8.209972d, 48.048320d))
                .setDeparture("Am Großhausberg 8")
                .setArrivalCoordinate(new Point(7.950507d, 48.088204d))
                .setArrival("Sick AG")
                .setLanguage(Locale.forLanguageTag("de"))
                .build();
    }

}
