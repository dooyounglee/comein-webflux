package com.doo.comein.exchange;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

	@Mock
	private ExchangeRepository exchangeRepository;
	
	@InjectMocks
	private ExchangeService exchangeService;
	
	@Test
	void listExchangeTest() {
		
		String id = "userId";
		List<Exchange> exchangeFlux = List.of(
				new Exchange(),
				new Exchange(),
				new Exchange());
		
		when(exchangeRepository.findByUserId(id)).thenReturn(Flux.fromIterable(exchangeFlux));
		
		StepVerifier.create(exchangeService.list(id))
		.expectNextCount(3)
		.verifyComplete();
	}
	
	@Test
	void listExchangeNotFoundTest() {
		
		String id = "userId";
		
		when(exchangeRepository.findByUserId(id)).thenReturn(Flux.empty());
		
		StepVerifier.create(exchangeService.list(id))
		//.expectNextCount(0)
		.verifyComplete();
	}
}
