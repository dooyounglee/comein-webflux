package com.doo.comein.exchange;

import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExchangeRouter.class, ExchangeHandler.class})
@WebFluxTest
public class ExchangeHandlerTest {

	final static Logger logger = LoggerFactory.getLogger(ExchangeHandlerTest.class);
	@Autowired private ApplicationContext context;
	private MediaType json = MediaType.APPLICATION_JSON;
	private WebTestClient client;
	
	@MockBean ExchangeService exchangeService;
	
	@BeforeEach
	void setup() {
		client = WebTestClient.bindToApplicationContext(context).build();
	}
	
	@Test
	@DisplayName("get_exchangeList success")
	@Order(1)
	void setCustomerListTest() {

		// given
		String id = "userId";
		List<Exchange> exchangeFlux = List.of(
				new Exchange("id1","userId","","",0,0,0,0,"","","","","",""),
				new Exchange("id2","userId","","",0,0,0,0,"","","","","",""),
				new Exchange("id3","userId","","",0,0,0,0,"","","","","",""));
		
		// when
		when(exchangeService.list(id)).thenReturn(Flux.fromIterable(exchangeFlux));
		
		// then
		client.get()
			.uri(URI.create("/exchange/userId"))
			.accept(json)
			// .body("userId", String.class)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Exchange.class)
			.value(result -> Assertions.assertThat(result).isEqualTo(exchangeFlux))
			;
	}
	
	@Test
	@DisplayName("get_exchangeList_not_found")
	@Order(2)
	void setCustomerNotFoundTest() {

		// given
		String id = "userId";
		
		// when
		when(exchangeService.list(id)).thenReturn(Flux.empty());
		
		// then
		client.get()
			.uri(URI.create("/exchange/userId"))
			.accept(json)
			// .body("userId", String.class)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Exchange.class)
			.value(result -> Assertions.assertThat(result).isEqualTo(List.of()))
			;
	}
	
	@Test
	@DisplayName("add_exchange_success")
	@Order(3)
	void postExchangeSuccess() {
		
		// given
		Exchange exchange = new Exchange();
		
		// when
		when(exchangeService.add(exchange)).thenReturn(Mono.just(exchange));
		
		// then
		client.post().uri(URI.create("/exchange/add")).accept(json)
		.body(Mono.just(exchange), Exchange.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(Exchange.class)
		.value(result -> Assertions.assertThat(result).isEqualTo(exchange))
		;
	}
}
