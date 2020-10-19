package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OtpMapperApiServiceTest {

    private final CallService callService = mock(CallService.class);

    private final OtpMapperApiService classUnderTest = new OtpMapperApiServiceImpl(callService);

    @BeforeEach
    void init() {
        JourneyOb
        when(callService.post(anyString(), anyString(), any(HttpHeaders.class)))
                .thenReturn(Flux.just())
    }

    @Test
    void test_() {

    }
}
