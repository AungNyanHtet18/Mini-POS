package com.anh.web.pos.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.anh.web.pos.domain.output.SaleInfo;

public interface SaleHistoryRepo {

	int create(String salePerson);

	List<SaleInfo> search(String salePerson, LocalDate from, LocalDate to);

	Optional<SaleInfo> findById(int id);

}
