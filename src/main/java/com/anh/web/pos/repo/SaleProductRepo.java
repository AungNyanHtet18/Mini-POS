package com.anh.web.pos.repo;

import java.util.List;

import com.anh.web.pos.domain.input.SaleItem;
import com.anh.web.pos.domain.output.SaleDetailsItem;

public interface SaleProductRepo {

	void creae(int saleId, SaleItem item);

	List<SaleDetailsItem> findBySaleId(int id);

}
