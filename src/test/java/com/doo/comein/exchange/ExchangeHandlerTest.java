package com.doo.comein.exchange;

import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
	void setCustomerListTest() {

		String id = "userId";
		List<Exchange> exchangeFlux = List.of(
				new Exchange("id1","userId","","",0,0,0,0,"","","","","",""),
				new Exchange("id2","userId","","",0,0,0,0,"","","","","",""),
				new Exchange("id3","userId","","",0,0,0,0,"","","","","",""));
		
		when(exchangeService.list(id)).thenReturn(Flux.fromIterable(exchangeFlux));
		
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
	void setCustomerNotFoundTest() {

		String id = "userId";
		
		when(exchangeService.list(id)).thenReturn(Flux.empty());
		
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
}
