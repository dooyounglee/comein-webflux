package com.doo.comein.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.doo.comein.exchange.dto.ExchangeRequest;
import com.doo.comein.exchange.dto.ExchangeResponse;

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
		
		return ServerResponse
				.ok().contentType(json).body(result, Exchange.class)
				;
	}
}
