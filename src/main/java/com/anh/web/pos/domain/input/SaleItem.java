package com.anh.web.pos.domain.input;

import lombok.Data;

@Data
public class SaleItem {

	private int saleId;
	
	private String productCode;
	private int unitPrice;
	private int quantity;
}
