package de.blackforestsolutions.dravelopsstargateservice.exceptionhandling;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import de.blackforestsolutions.dravelopsdatamodel.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class ExceptionHandlerServiceImpl implements ExceptionHandlerService {

    @Override
    public <T> Flux<T> handleExceptions(Throwable exception) {
        logError(exception);
        return Flux.empty();
    }

    @Override
    public <T> Mono<T> handleExceptions(CallStatus<T> callStatus) {
        if (Optional.ofNullable(callStatus.getThrowable()).isPresent()) {
            logError(callStatus);
            return Mono.empty();
        }
        if (Optional.ofNullable(callStatus.getStatus()).isEmpty()) {
            logMissingStatus();
            return Mono.empty();
        }
        if (callStatus.getStatus().equals(Status.FAILED)) {
            logMissingException();
            return Mono.empty();
        }
        if (Optional.ofNullable(callStatus.getCalledObject()).isPresent()) {
            return Mono.just(callStatus.getCalledObject());
        }
        if (callStatus.getStatus().equals(Status.SUCCESS)) {
            logMissingCalledObject();
            return Mono.empty();
        }
        return Mono.empty();
    }

    private static <T> void logError(CallStatus<T> callStatus) {
        log.error("Error during ApiServiceCall: ", callStatus.getThrowable());
    }

    private static void logError(Throwable e) {
        log.error("Error outside of ApiServiceCall: ", e);
    }

    private static void logMissingStatus() {
        log.warn("No Status for CallStatus found, eventually also no calledObject and exception");
    }

    private static void logMissingException() {
        log.warn("No Exception for failed CallStatus found");
    }


    private static void logMissingCalledObject() {
        log.warn("No CalledObject for failed CallStatus found");
    }
}
