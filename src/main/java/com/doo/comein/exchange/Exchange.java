package com.doo.comein.exchange;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "exchange")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

	@Id
	private String id;
	private String userId;
	private String type;
	private String targetId;
	private int myRed;
	private int myWhite;
	private int exRed;
	private int exWhite;
	private String fullYn;
	private String useYn;
	private String fromDt;
	private String toDt;
	private String matchingId;
	private String matchingStatus;
}
