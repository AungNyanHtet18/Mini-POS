package com.anh.web.pos.repo;

import java.util.Optional;

import com.anh.web.pos.domain.output.ProductDto;

public interface ProductRepo {

	Optional<ProductDto> findByCode(String code);

}
