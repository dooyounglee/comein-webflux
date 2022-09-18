package com.doo.comein.exchange.dto;

import com.doo.comein.exchange.Exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeDto {

	private String fromId;
	private String toId;
	private Exchange fromExchange;
	private Exchange toExchange;
	private String fromUserId;
	private String toUserId;
	private String status;
}
