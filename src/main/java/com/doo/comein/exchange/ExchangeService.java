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

	public Mono<Exchange> get(String id) {
		return exchangeRepository.findById(id);
	}
	
	public Mono<Exchange> add(Exchange b) {
		return exchangeRepository.save(b);
	}
	
	public void del(String id) {
		exchangeRepository.deleteById(id);
	}
	
	public Mono<Exchange> edit(Exchange b) {
		return exchangeRepository.save(b);
	}
	
	public Flux<Exchange> matchList(Exchange b) {
		if (b.getMyRed() != 0 && b.getExWhite() != 0) {
			return exchangeRepository.findExchangeByExchangeRW(b.getMyRed(), b.getExWhite());
		} else if (b.getMyWhite() != 0 && b.getExRed() != 0){
			return exchangeRepository.findExchangeByExchangeWR(b.getMyWhite(), b.getExRed());
		} else {
			return Flux.empty();
		}
		
	}
}
