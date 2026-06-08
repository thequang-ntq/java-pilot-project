package com.ntq.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.dto.request.ProductRequest;
import com.ntq.demo.dto.response.ProductResponse;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.entity.ProductEntity;
import com.ntq.demo.mapper.ProductMapper;
import com.ntq.demo.model.PageResponse;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.repository.BrandRepository;
import com.ntq.demo.repository.ProductRepository;

/**
 * Unit Tests for ProductServiceImpl
 *
 * @author Quang
 * @since 2026-06-01
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceImpl Tests")
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private BrandRepository brandRepository;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductServiceImpl productService;

	private ProductEntity testProduct;
	private ProductRequest testProductRequest;
	private ProductResponse testProductResponse;
	private BrandEntity testBrand;

	@BeforeEach
	void setUp() {
		/**
		 * Setup test data
		 */
		testBrand = new BrandEntity();
		testBrand.setBrandId(1);
		testBrand.setBrandName("Apple");

		testProduct = new ProductEntity();
		testProduct.setProductId(1);
		testProduct.setProductName("iPhone 15");
		testProduct.setQuantity(100);
		testProduct.setPrice(new BigDecimal("999.99"));
		testProduct.setBrand(testBrand);
		testProduct.setSaleDate(LocalDate.now());
		testProduct.setImage("images/product/iphone15.jpg");
		testProduct.setDescription("Latest iPhone");

		testProductRequest = new ProductRequest();
		testProductRequest.setProductName("iPhone 15");
		testProductRequest.setQuantity(100);
		testProductRequest.setPrice(new BigDecimal("999.99"));
		testProductRequest.setBrandId(1);
		testProductRequest.setSaleDate(LocalDate.now());
		testProductRequest.setDescription("Latest iPhone");

		testProductResponse = new ProductResponse();
		testProductResponse.setProductId(1);
		testProductResponse.setProductName("iPhone 15");
		testProductResponse.setQuantity(100);
		testProductResponse.setPrice(new BigDecimal("999.99"));
		testProductResponse.setBrandId(1);
		testProductResponse.setBrandName("Apple");
		testProductResponse.setSaleDate(LocalDate.now());
		testProductResponse.setImage("images/product/iphone15.jpg");
		testProductResponse.setDescription("Latest iPhone");
	}

	@Test
	@DisplayName("Should get list of products with pagination")
	void testGetList_Success() {
		int page = 1;
		String keyword = "";
		BigDecimal priceFrom = null;
		BigDecimal priceTo = null;

		List<ProductEntity> products = new ArrayList<>();
		products.add(testProduct);

		Page<ProductEntity> pageResult = new PageImpl<>(
			products,
			org.springframework.data.domain.PageRequest.of(0, 5),
			1
		);

		when(productRepository.searchProducts(anyString(), any(), any(), any(Pageable.class)))
			.thenReturn(pageResult);
		when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

		ResponseDataModel<?> response = productService.getList(page, keyword, priceFrom, priceTo);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Success", response.getResponseMsg());

		PageResponse<?> pageData = (PageResponse<?>) response.getData();
		assertNotNull(pageData);
		assertEquals(1, pageData.getTotalPages());
		assertEquals(1, pageData.getContent().size());

		verify(productRepository, times(1)).searchProducts(anyString(), any(), any(), any(Pageable.class));
		verify(productMapper, times(1)).toResponse(testProduct);
	}

	@Test
	@DisplayName("Should search products by keyword and price range")
	void testGetList_WithFilters() {
		String keyword = "iPhone";
		BigDecimal priceFrom = new BigDecimal("500");
		BigDecimal priceTo = new BigDecimal("1500");

		List<ProductEntity> products = new ArrayList<>();
		products.add(testProduct);

		Page<ProductEntity> pageResult = new PageImpl<>(
			products,
			org.springframework.data.domain.PageRequest.of(0, 5),
			1
		);

		when(productRepository.searchProducts(eq(keyword), eq(priceFrom), eq(priceTo), any(Pageable.class)))
			.thenReturn(pageResult);
		when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

		ResponseDataModel<?> response = productService.getList(1, keyword, priceFrom, priceTo);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());

		verify(productRepository, times(1)).searchProducts(eq(keyword), eq(priceFrom), eq(priceTo), any(Pageable.class));
	}

	@Test
	@DisplayName("Should get product by ID")
	void testGetById_Success() {
		when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

		ResponseDataModel<?> response = productService.getById(1);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Success", response.getResponseMsg());

		ProductResponse data = (ProductResponse) response.getData();
		assertNotNull(data);
		assertEquals(1, data.getProductId());
		assertEquals("iPhone 15", data.getProductName());
		assertEquals("Apple", data.getBrandName());

		verify(productRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("Should return not found when product ID doesn't exist")
	void testGetById_NotFound() {
		when(productRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = productService.getById(999);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Product not found", response.getResponseMsg());
		assertNull(response.getData());

		verify(productRepository, times(1)).findById(999);
	}

	@Test
	@DisplayName("Should add product successfully")
	void testAdd_Success() {
		when(productRepository.existsByProductName("iPhone 15")).thenReturn(false);
		when(brandRepository.findById(1)).thenReturn(Optional.of(testBrand));
		when(productMapper.toEntity(testProductRequest, testBrand)).thenReturn(testProduct);
		when(productRepository.saveAndFlush(testProduct)).thenReturn(testProduct);
		when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

		ResponseDataModel<?> response = productService.add(testProductRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Product is added successfully", response.getResponseMsg());

		ProductResponse data = (ProductResponse) response.getData();
		assertNotNull(data);
		assertEquals("iPhone 15", data.getProductName());

		verify(productRepository, times(1)).existsByProductName("iPhone 15");
		verify(brandRepository, times(1)).findById(1);
		verify(productRepository, times(1)).saveAndFlush(testProduct);
	}

	@Test
	@DisplayName("Should return duplicate error when product name exists")
	void testAdd_DuplicateName() {
		when(productRepository.existsByProductName("iPhone 15")).thenReturn(true);

		ResponseDataModel<?> response = productService.add(testProductRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_DUPL, response.getResponseCode());
		assertEquals("Product name already exists", response.getResponseMsg());

		verify(productRepository, times(1)).existsByProductName("iPhone 15");
		verify(productMapper, never()).toEntity(any(), any());
	}

	@Test
	@DisplayName("Should return error when brand doesn't exist")
	void testAdd_BrandNotFound() {
		testProductRequest.setBrandId(999);
		when(productRepository.existsByProductName("iPhone 15")).thenReturn(false);
		when(brandRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = productService.add(testProductRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Brand not found", response.getResponseMsg());

		verify(productRepository, times(1)).existsByProductName("iPhone 15");
		verify(brandRepository, times(1)).findById(999);
		verify(productMapper, never()).toEntity(any(), any());
	}

	@Test
	@DisplayName("Should update product successfully")
	void testUpdate_Success() {
		when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
//		when(productRepository.existsByProductName("iPhone 15")).thenReturn(false);
		when(brandRepository.findById(1)).thenReturn(Optional.of(testBrand));
		doNothing().when(productMapper).updateEntity(testProductRequest, testProduct, testBrand);
		when(productRepository.saveAndFlush(testProduct)).thenReturn(testProduct);
		when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

		ResponseDataModel<?> response = productService.update(1, testProductRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Product is updated successfully", response.getResponseMsg());

		verify(productRepository, times(1)).findById(1);
		verify(productMapper, times(1)).updateEntity(testProductRequest, testProduct, testBrand);
		verify(productRepository, times(1)).saveAndFlush(testProduct);
	}

	@Test
	@DisplayName("Should return not found when updating non-existent product")
	void testUpdate_NotFound() {
		when(productRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = productService.update(999, testProductRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Product not found", response.getResponseMsg());

		verify(productRepository, times(1)).findById(999);
		verify(productMapper, never()).updateEntity(any(), any(), any());
	}

	@Test
	@DisplayName("Should delete product successfully")
	void testDelete_Success() {
		when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
		doNothing().when(productRepository).delete(testProduct);
		doNothing().when(productRepository).flush();

		try (var mockStatic = mockStatic(FileHelper.class)) {
			mockStatic.when(() -> FileHelper.deleteFile(anyString())).then(invocation -> null);

			ResponseDataModel<?> response = productService.delete(1);

			assertNotNull(response);
			assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
			assertEquals("Product is deleted successfully", response.getResponseMsg());

			verify(productRepository, times(1)).findById(1);
			verify(productRepository, times(1)).delete(testProduct);
			verify(productRepository, times(1)).flush();
		}
	}

	@Test
	@DisplayName("Should return not found when deleting non-existent product")
	void testDelete_NotFound() {
		when(productRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = productService.delete(999);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Product not found", response.getResponseMsg());

		verify(productRepository, times(1)).findById(999);
		verify(productRepository, never()).delete(any());
	}
}