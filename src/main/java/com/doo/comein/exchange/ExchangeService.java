package com.doo.comein.exchange;

import org.springframework.stereotype.Service;

import com.doo.comein.exchange.dto.ExchangeRequest;

import reactor.core.publisher.Flux;

@Service
public class ExchangeService {

	private final ExchangeRepository exchangeRepository;
	
	public ExchangeService(ExchangeRepository exchangeRepository) {
		this.exchangeRepository = exchangeRepository;
	}
	
	public Flux<Exchange> list(String id) {
		return exchangeRepository.findByUserId(id);
	}
}
