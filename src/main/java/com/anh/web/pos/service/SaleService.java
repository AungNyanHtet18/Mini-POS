package com.anh.web.pos.service;

import java.time.LocalDate;
import java.util.List;

import com.anh.web.pos.domain.input.ShoppingCart;
import com.anh.web.pos.domain.output.SaleDetails;
import com.anh.web.pos.domain.output.SaleInfo;

public interface SaleService {

	int checkOut(ShoppingCart cart);

	List<SaleInfo> search(String salePerson, LocalDate from, LocalDate to);

	SaleDetails findById(int id);
}
