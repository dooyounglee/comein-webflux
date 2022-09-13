package com.doo.comein.exchange;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ExchangeRouter {

	private final MediaType json = MediaType.APPLICATION_JSON;
	
	@Bean
	public RouterFunction<ServerResponse> exchangeEndpoint(ExchangeHandler handler) {
		return RouterFunctions
				   .route(GET("/exchange/{id}").and(accept(json)), handler::list)
				//.andRoute()
				;
	}
}
