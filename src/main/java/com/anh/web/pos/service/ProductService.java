package com.anh.web.pos.service;

import com.anh.web.pos.domain.output.ProductDto;

public interface ProductService {

	ProductDto findByCode(String code);
}
