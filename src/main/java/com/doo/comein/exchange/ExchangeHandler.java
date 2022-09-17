package com.doo.comein.exchange;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.doo.comein.user.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ExchangeHandler {

	final static Logger logger = LoggerFactory.getLogger(ExchangeHandler.class);
	private MediaType json = MediaType.APPLICATION_JSON;
	
	private final ExchangeService exchangeService;
	
	public ExchangeHandler(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;
	}
	
	public Mono<ServerResponse> list(ServerRequest request) {
		
		Mono<User> user = request.session().flatMap(session -> {
			return Mono.just((User) session.getAttributes().get("user"));
		});
		Flux<Exchange> result = user.flatMapMany(b -> exchangeService.list(b.getId()));
		
		return ServerResponse.ok().contentType(json)
				.body(result, List.class)
				;
		
	}
	
	public Mono<ServerResponse> get(ServerRequest request) {
		
		String id = request.pathVariable("id");
		Mono<Exchange> result = exchangeService.get(id);
		
		return result.flatMap(exchange -> ServerResponse.ok().contentType(json).bodyValue(exchange))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> add(ServerRequest request) {
		
		Mono<Exchange> body = request.bodyToMono(Exchange.class);
		Mono<Exchange> result = body.flatMap(b -> exchangeService.add(b));
		
		return ServerResponse.ok().contentType(json)
				.body(result, Exchange.class)
				;
	}
	
	public Mono<ServerResponse> del(ServerRequest request) {
		
		String id = request.pathVariable("id");
		exchangeService.del(id);
		
		return ServerResponse.ok().contentType(json)
				.build()
				;
	}
	
	public Mono<ServerResponse> edit(ServerRequest request) {
		
		Mono<Exchange> body = request.bodyToMono(Exchange.class);
		Mono<Exchange> result = body.flatMap(b -> exchangeService.edit(b));
		
		return ServerResponse.ok().contentType(json)
				.body(result, Exchange.class)
				;
	}
	
	public Mono<ServerResponse> matching(ServerRequest request) {
		
		String id = request.pathVariable("id");
		Mono<Exchange> exchange = exchangeService.get(id);
		Flux<Exchange> exchangeList = exchange.flatMapMany(b -> exchangeService.matchList(b));
		
		return ServerResponse.ok().contentType(json)
				.body(exchangeList, List.class)
				;
				
	}
	
	
	
	
	
	
	
	
	
	
//	@Deprecated
//	public Mono<ServerResponse> setSession(ServerRequest request) {
//		logger.debug("setSession");
//		return request.session().flatMap(session -> {
//			session.getAttributes().put("user", new User("userId","11","111"));
//			return ServerResponse.ok().contentType(json)
//					.bodyValue(session.getAttributes().get("user"));
//		});
//	}
//	
//	@Deprecated
//	public Mono<ServerResponse> getSession(ServerRequest request) {
//		logger.debug("getSession");
//		return request.session().flatMap(session -> {
//			return ServerResponse.ok().contentType(json)
//					.bodyValue(session.getAttributes().get("user"));
//		});
//	}
}
