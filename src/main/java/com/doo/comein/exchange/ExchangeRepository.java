package com.doo.comein.exchange;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface ExchangeRepository extends ReactiveMongoRepository<Exchange, String>{

	Flux<Exchange> findByUserId(String id);
	
	@Query(value="{'myWhite' : { $gte: ?1 }, 'exRed' : { $lte: ?0 }}")
	Flux<Exchange> findExchangeByExchangeRW(int myRed, int exWhite);
	
	@Query(value="{'myRed' : { $gte: ?1 }, 'exWhite' : { $lte: ?0 }}")
	Flux<Exchange> findExchangeByExchangeWR(int myWhite, int exRed);

}
