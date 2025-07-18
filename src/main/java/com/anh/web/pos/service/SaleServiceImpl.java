package com.anh.web.pos.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.anh.web.pos.domain.exceptions.PosBusinessException;
import com.anh.web.pos.domain.input.ShoppingCart;
import com.anh.web.pos.domain.output.SaleDetails;
import com.anh.web.pos.domain.output.SaleInfo;
import com.anh.web.pos.repo.ProductRepo;
import com.anh.web.pos.repo.SaleHistoryRepo;
import com.anh.web.pos.repo.SaleProductRepo;

@Service
public class SaleServiceImpl implements SaleService {
	
	@Autowired
	private SaleHistoryRepo saleHistoryRepo;
	@Autowired
	private SaleProductRepo saleProductRepo;
	@Autowired
	private ProductRepo productRepo;

	@Override
	public int checkOut(ShoppingCart cart) {
		
		validate(cart);
		
		// create sale history
		var saleId = saleHistoryRepo.create(cart.getSalePerson());
		
		for(var item : cart.getItems()) {
			saleProductRepo.creae(saleId, item);
		}
		
		return saleId;
	}

	private void validate(ShoppingCart cart) {
		
		if(null == cart) {
			throw new PosBusinessException("Cart must not be null.");
		}
		
		if(!StringUtils.hasLength(cart.getSalePerson())) {
			throw new PosBusinessException("Please enter sale person name.");
		}
		
		if(null == cart.getItems() || cart.getItems().isEmpty()) {
			throw new PosBusinessException("Please enter sale items.");
		}
		
		for(var item : cart.getItems()) {
			if(!StringUtils.hasLength(item.getProductCode())) {
				throw new PosBusinessException("Please enter product code.");
			}
			
			if(productRepo.findByCode(item.getProductCode()).isEmpty()) {
				throw new PosBusinessException("Invalid product code.");
			}
			
			if(item.getUnitPrice() <= 0) {
				throw new PosBusinessException("Please enter unit price.");
			}

			if(item.getQuantity() <= 0) {
				throw new PosBusinessException("Please enter quantity.");
			}
		}
		
	}

	@Override
	public List<SaleInfo> search(String salePerson, LocalDate from, LocalDate to) {
		return saleHistoryRepo.search(salePerson, from, to);
	}

	@Override
	public SaleDetails findById(int id) {
		
		var saleInfo = saleHistoryRepo.findById(id)
				.orElseThrow(() -> new PosBusinessException("Invalid sale id."));
		
		var items = saleProductRepo.findBySaleId(id);
		
		return SaleDetails.from(saleInfo, items);
	}

}
