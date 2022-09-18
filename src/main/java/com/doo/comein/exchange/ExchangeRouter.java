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
				   .route(GET("/exchange/list").and(accept(json)), handler::list)
				.andRoute(GET("/exchange/{id}").and(accept(json)), handler::get)
				.andRoute(POST("/exchange/add").and(accept(json)), handler::add)
				.andRoute(DELETE("/exchange/{id}").and(accept(json)), handler::del)
				.andRoute(PUT("/exchange").and(accept(json)), handler::edit)
				
				.andRoute(GET("/exchange/matching/{id}").and(accept(json)), handler::matching)
				.andRoute(PUT("/exchange/match/request").and(accept(json)), handler::requestMatch)
				.andRoute(PUT("/exchange/match/accept").and(accept(json)), handler::acceptMatch)
				
//				.andRoute(GET("/setsession").and(accept(json)), handler::setSession)
//				.andRoute(GET("/getsession").and(accept(json)), handler::getSession)
				;
	}
}
