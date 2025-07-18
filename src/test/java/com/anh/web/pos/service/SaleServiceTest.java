package com.anh.web.pos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.anh.web.pos.domain.exceptions.PosBusinessException;
import com.anh.web.pos.domain.input.ShoppingCart;
import com.anh.web.pos.service.data.ErrorForSaleItemProvider;
import com.anh.web.pos.service.data.SaleServiceCreateSuccess;
import com.anh.web.pos.service.data.SaleServiceFindByIdSuccess;
import com.anh.web.pos.service.data.SaleServiceFindByIdSuccess.SaleItem;

@SpringBootTest
@TestMethodOrder(value = OrderAnnotation.class)
public class SaleServiceTest {
	
	@Autowired
	private SaleService service;

	@Order(1)
	@NullSource
	@ParameterizedTest
	void test_error_null_cart(ShoppingCart cart) {
		
		var exception = assertThrows(PosBusinessException.class, 
				() -> service.checkOut(null));
		
		assertEquals("Cart must not be null.", exception.getMessage());
	}
	
	@Order(2)
	@MethodSource
	@ParameterizedTest
	void test_error_empty_sale_person(ShoppingCart cart) {
		
		var exception = assertThrows(PosBusinessException.class, 
				() -> service.checkOut(cart));
		
		assertEquals("Please enter sale person name.", exception.getMessage());
	}
	
	static List<ShoppingCart> test_error_empty_sale_person() {
		return List.of(new ShoppingCart());
	}
	
	@Order(3)
	@MethodSource
	@ParameterizedTest
	void test_error_null_items(ShoppingCart cart) {
		
		var exception = assertThrows(PosBusinessException.class, 
				() -> service.checkOut(cart));
		
		assertEquals("Please enter sale items.", exception.getMessage());
	}
	
	static List<ShoppingCart> test_error_null_items() {
		return List.of(ShoppingCart.withName("Aung Aung"));
	}
	
	@Order(4)
	@ParameterizedTest
	@ArgumentsSource(value = ErrorForSaleItemProvider.class)
	void test_error_for_sale_item(ShoppingCart cart, String message) {
		var exception = assertThrows(PosBusinessException.class, 
				() -> service.checkOut(cart));
		assertEquals(message, exception.getMessage());
	}
	
	@Order(5)
	@ParameterizedTest
	@ArgumentsSource(value = SaleServiceCreateSuccess.class)
	void test_create_success(ShoppingCart cart, int id) {
		var result = service.checkOut(cart);
		assertEquals(id, result);
	}
	
	@Order(6)
	@ParameterizedTest
	@ValueSource(ints = {0, 4})
	void test_find_by_id_notfound(int id) {
		
		var ex = assertThrows(PosBusinessException.class, 
				() -> service.findById(id));
		
		assertEquals("Invalid sale id.", ex.getMessage());
	}
	

	@Order(7)
	@ParameterizedTest
	@ArgumentsSource(value = SaleServiceFindByIdSuccess.class)
	void test_find_by_id(int id, String person, List<SaleItem> items) {
		
		var result = service.findById(id);
		
		assertNotNull(result);
		
		assertEquals(id, result.getId());
		assertEquals(person, result.getSalePerson());
		assertNotNull(result.getSaleAt());
		
		assertEquals(items.size(), result.getItems().size());
		
		for(var i = 0; i < items.size(); i ++) {
			
			var expected = items.get(i);
			var actual = result.getItems().get(i);
			
			assertEquals(id, actual.getId(), "Sale ID for index [%d] is not match. Expected [%d] : Actual [%d]".formatted(i, id, actual.getId()));
			assertEquals(expected.code(), actual.getProductCode(), "Product Code for index [%d] is not match. Expected [%s] : Actual [%s]".formatted(i, expected.code(), actual.getProductCode()));
			assertEquals(expected.name(), actual.getProductName(), "Product Name for index [%d] is not match. Expected [%s] : Actual [%s]".formatted(i, expected.name(), actual.getProductName()));
			assertEquals(expected.price(), actual.getUnitPrice(), "Unit Price for index [%d] is not match. Expected [%d] : Actual [%d]".formatted(i, expected.price(), actual.getUnitPrice()));
			assertEquals(expected.quantity(), actual.getQuantity(), "Quantity for index [%d] is not match. Expected [%d] : Actual [%d]".formatted(i, expected.quantity(), actual.getQuantity()));
			
		}
	}
	
	
	@Order(8)
	@MethodSource
	@ParameterizedTest
	void test_search(String person, LocalDate from, LocalDate to, int size) {
		var result = service.search(person, from, to);
		assertEquals(size, result.size());
	}
	
	public static Stream<Arguments> test_search() {
		return Stream.of(
			Arguments.of(null, null, null, 3),
			Arguments.of("Thidar", null, null, 2),
			Arguments.of("", LocalDate.now(), null, 3),
			Arguments.of("Nilar", LocalDate.now(), null, 1),
			Arguments.of("", null ,LocalDate.now(), 3),
			Arguments.of("", null ,LocalDate.now().minusDays(1), 0)
		);
	}
}
