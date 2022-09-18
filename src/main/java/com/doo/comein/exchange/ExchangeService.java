package com.doo.comein.exchange;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.doo.comein.exchange.dto.ExchangeDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExchangeService {

	final static Logger logger = LoggerFactory.getLogger(ExchangeService.class);
	
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

	public Mono<Map<String, Object>> requestMatch(Map<String, Object> map) {
		logger.debug("com.doo.comein.exchange.ExchangeService.requestMath.map : {}", map);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		String fromId = (String) map.get("fromId");
		String toId = (String) map.get("toId");
		Mono<Exchange> fromExchange = exchangeRepository.findById(fromId);
		fromExchange.flatMap(b -> {
			returnMap.put("fromUserId", b.getUserId());
			
			b.setMatchingId(toId);
			b.setMatchingStatus("R"); // R:요청 S:수락 W:수락대기
			return exchangeRepository.save(b);
		});
		
		Mono<Exchange> toExchange = exchangeRepository.findById(toId);
		toExchange.flatMap(b -> {
			returnMap.put("toUserId", b.getUserId());
			
			b.setMatchingId(fromId);
			b.setMatchingStatus("W"); // R:요청 S:수락 W:수락대기
			return exchangeRepository.save(b);
		});
		
		return Mono.just(returnMap);
	}
	
	public Mono<ExchangeDto> acceptMatch(ExchangeDto exchangeDto) {
		logger.debug("com.doo.comein.exchange.ExchangeService.requestMath.exchangeDto : {}", exchangeDto);
		
		String fromId = exchangeDto.getFromId();
		String toId = exchangeDto.getToId();
		Mono<Exchange> fromExchange = exchangeRepository.findById(fromId);
		fromExchange.flatMap(b -> {
			exchangeDto.setFromUserId(b.getUserId());
			
			b.setMatchingId(toId);
			b.setMatchingStatus("R"); // R:요청 S:수락 W:수락대기
			return exchangeRepository.save(b);
		});
		
		Mono<Exchange> toExchange = exchangeRepository.findById(toId);
		toExchange.flatMap(b -> {
			exchangeDto.setToUserId(b.getUserId());
			
			b.setMatchingId(fromId);
			b.setMatchingStatus("W"); // R:요청 S:수락 W:수락대기
			return exchangeRepository.save(b);
		});
		
		exchangeDto.setStatus("RW");
		
		return Mono.just(exchangeDto);
	}
}
