package com.doo.comein.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import com.doo.comein.user.User;

import reactor.core.publisher.Mono;

@Component
public class WebTestClientConfig implements WebTestClientConfigurer {

	private static Map<String, Object> sessionMap;
	
	private User user = new User("userId", "username", "useremail");
	
	@Override
	public void afterConfigurerAdded(Builder builder, WebHttpHandlerBuilder httpHandlerBuilder,
			ClientHttpConnector connector) {
		final SessionMutatorFilter sessionMutatorFilter = new SessionMutatorFilter();
        httpHandlerBuilder.filters(filters -> filters.add(0, sessionMutatorFilter));
	}
	
	class SessionMutatorFilter implements WebFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain webFilterChain) {
        	
        	sessionMap = new HashMap<String, Object>();
        	sessionMap.put("user", user);
        	System.out.println(user);
        	
            return exchange.getSession()
                           .doOnNext(webSession -> webSession.getAttributes().putAll(sessionMap))
                           .then(webFilterChain.filter(exchange));
        }
    }
}
