package de.blackforestsolutions.dravelopsstargateservice.exceptionhandling;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExceptionHandlerService {
    <T> Flux<T> handleExceptions(Throwable exception);

    <T> Mono<T> handleExceptions(CallStatus<T> callStatus);
}
