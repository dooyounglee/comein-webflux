package com.doo.comein.exchange;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface ExchangeRepository extends ReactiveMongoRepository<Exchange, String>{

	Flux<Exchange> findByUserId(String id);

}
