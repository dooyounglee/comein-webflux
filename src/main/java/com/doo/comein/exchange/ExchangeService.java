package com.doo.comein.exchange;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExchangeService {

	private final ExchangeRepository exchangeRepository;
	
	public ExchangeService(ExchangeRepository exchangeRepository) {
		this.exchangeRepository = exchangeRepository;
	}
	
	public Flux<Exchange> list(String id) {
		return exchangeRepository.findByUserId(id);
	}

	public Mono<Exchange> add(Exchange b) {
		return exchangeRepository.save(b);
	}
}
