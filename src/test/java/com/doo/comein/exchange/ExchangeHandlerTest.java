package com.doo.comein.exchange;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.WebSessionManager;

import com.doo.comein.config.WebTestClientConfig;
import com.doo.comein.user.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExchangeRouter.class, ExchangeHandler.class})
@WebFluxTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExchangeHandlerTest {

	final static Logger logger = LoggerFactory.getLogger(ExchangeHandlerTest.class);
	@Autowired private ApplicationContext context;
	private MediaType json = MediaType.APPLICATION_JSON;
	private WebTestClient client;
	private WebTestClientConfig webTestClientConfig = new WebTestClientConfig();
	
	@MockBean ExchangeService exchangeService;
	
	@BeforeEach
	void setup() {
		client = WebTestClient./*bindToWebHandler(exchange -> {
			return exchange.getSession()
					.doOnNext(webSession -> webSession.getAttributes().put("user", 
							new User("userId", "username", "useremail"))
							)
					.then();
		})*/
				bindToApplicationContext(context )
				.webSessionManager(new WebSessionManager() {
					
					@Override
					public Mono<WebSession> getSession(ServerWebExchange exchange) {
						return exchange.getSession().map(
							session -> {
								session.getAttributes().put("user", new User("userId", "username", "useremail"));
								return session;
							}
						);
					}
					
				})
				.build();
		
//		client.get().uri(URI.create("/session")).accept(json)
//		.exchange()
//		;
	}
	
	@Test
	@DisplayName("1. get_exchangeList success")
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
		client.mutateWith(webTestClientConfig).get()
			.uri(URI.create("/exchange/list"))
			.accept(json)
			// .body("userId", String.class)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Exchange.class)
			.value(result -> Assertions.assertThat(result).isEqualTo(exchangeFlux))
			;
	}
	
	@Test
	@DisplayName("2. get_exchangeList_not_found")
	@Order(2)
	void setCustomerNotFoundTest() {

		// given
		String id = "userId";
		
		// when
		when(exchangeService.list(id)).thenReturn(Flux.empty());
		
		// then
		client.mutateWith(webTestClientConfig).get()
			.uri(URI.create("/exchange/list"))
			.accept(json)
			// .body("userId", String.class)
			.exchange()
			.expectStatus().isOk()
			.expectBody(List.class)
			.value(result -> Assertions.assertThat(result).isEmpty())
			;
	}
	
	@Test
	@DisplayName("3. add_exchange_success")
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
	
	@Test
	@DisplayName("get_exchange_success")
	@Order(4)
	void getExchangeSuccess() {
		
		// given
		String id = "exchangeId";
		Exchange exchange = new Exchange();
		
		// when
		when(exchangeService.get(id)).thenReturn(Mono.just(exchange));
		
		// then
		client.get().uri(URI.create("/exchange/" + id)).accept(json)
		.exchange()
		.expectStatus().isOk()
		.expectBody(Exchange.class)
		.value(result -> Assertions.assertThat(result).isEqualTo(exchange))
		;
	}
	
	@Test
	@DisplayName("get_exchange_notfound")
	@Order(5)
	void getExchange_not_found() {
		
		// given
		String id = "exchangeId";
		
		// when
		when(exchangeService.get(id)).thenReturn(Mono.empty());
		
		// then
		client.get().uri(URI.create("/exchange/" + id)).accept(json)
		.exchange()
		.expectStatus().isNotFound()
		;
	}
	
	@Test
	@DisplayName("delete_exchange_success")
	@Order(6)
	void delete_exchange_Success() {
		
		// given
		String id = "exchangeId";
		
		// when
		doNothing().when(exchangeService).del(id);
		
		// then
		client.delete().uri(URI.create("/exchange/" + id)).accept(json)
		.exchange()
		.expectStatus().isOk()
		;
	}
	
	@Test
	@DisplayName("edit_exchange_success")
	@Order(7)
	void edit_exchange_success() {
		
		// given
		Exchange exchange = new Exchange();
		
		// when
		when(exchangeService.edit(exchange)).thenReturn(Mono.just(exchange));
		
		// then
		client.put().uri(URI.create("/exchange")).accept(json)
		.body(Mono.just(exchange), Exchange.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(Exchange.class)
		.value(result -> Assertions.assertThat(result).isEqualTo(exchange))
		;
	}
	
//	@Test
//	@DisplayName("4. session")
//	@Order(4)
//	void sessionTest() {
//		client.get().uri(URI.create("/session")).accept(json)
//		.exchange()
//		.expectStatus().isOk()
//		.expectBody(User.class)
//		.value(result -> {
//			System.out.println("ddddddddddddddd"+result);
//		});
//	}
//	
//	@Test
//	@DisplayName("5. sessionCheck")
//	@Order(5)
//	void sessionCheckTest() {
//		client.mutateWith(webTestClientConfig)
//		.get().uri(URI.create("/exchange")).accept(json)
//		.exchange()
//		.expectStatus().isOk()
//		.expectBody(Map.class)
//		.value(result -> {
//			System.out.println("eeeeeeeeeeeeeee"+result);
//		});
//		;
//	}
}
