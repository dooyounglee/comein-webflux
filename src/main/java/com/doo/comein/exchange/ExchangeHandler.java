package com.doo.comein.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ExchangeHandler {

	final static Logger logger = LoggerFactory.getLogger(ExchangeHandler.class);
	private MediaType json = MediaType.APPLICATION_JSON;
	
	private final ExchangeService exchangeService;
	
	public ExchangeHandler(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;
	}
	
	public Mono<ServerResponse> list(ServerRequest request) {
		
		String id = request.pathVariable("id");
		Flux<Exchange> result = exchangeService.list(id);
		
		return ServerResponse.ok().contentType(json)
				.body(result, Exchange.class)
				;
	}
	
	public Mono<ServerResponse> addPost(ServerRequest request) {
		
		Mono<Exchange> body = request.bodyToMono(Exchange.class);
		Mono<Exchange> result = body.flatMap(b -> exchangeService.add(b));
		
		return ServerResponse.ok().contentType(json)
				.body(result, Exchange.class)
				;
	}
}
