package com.ntq.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.dto.request.BrandRequest;
import com.ntq.demo.dto.response.BrandResponse;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.entity.ProductEntity;
import com.ntq.demo.mapper.BrandMapper;
import com.ntq.demo.model.PageResponse;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.repository.BrandRepository;

/**
 * Unit Tests for BrandServiceImpl
 *
 * @author Quang
 * @since 2026-06-01
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BrandServiceImpl Tests")
class BrandServiceImplTest {

	@Mock
	private BrandRepository brandRepository;

	@Mock
	private BrandMapper brandMapper;

	@InjectMocks
	private BrandServiceImpl brandService;

	private BrandEntity testBrand;
	private BrandRequest testBrandRequest;
	private BrandResponse testBrandResponse;

	@BeforeEach
	void setUp() {
		/**
		 * Setup test data
		 */
		testBrand = new BrandEntity();
		testBrand.setBrandId(1);
		testBrand.setBrandName("Apple");
		testBrand.setLogo("images/brand/apple.png");
		testBrand.setDescription("Apple Inc");

		testBrandRequest = new BrandRequest();
		testBrandRequest.setBrandName("Apple");
		testBrandRequest.setDescription("Apple Inc");

		testBrandResponse = new BrandResponse();
		testBrandResponse.setBrandId(1);
		testBrandResponse.setBrandName("Apple");
		testBrandResponse.setLogo("images/brand/apple.png");
		testBrandResponse.setDescription("Apple Inc");
	}

	@Test
	@DisplayName("Should get list of brands with pagination")
	void testGetList_Success() {
		/**
		 * Arrange
		 */
		int page = 1;
		String keyword = "";
		List<BrandEntity> brands = new ArrayList<>();
		brands.add(testBrand);

		Page<BrandEntity> pageResult = new PageImpl<>(brands, PageRequest.of(0, 5), 1);

		when(brandRepository.findByBrandNameContainingIgnoreCase(anyString(), any(Pageable.class)))
			.thenReturn(pageResult);
		when(brandMapper.toResponse(testBrand)).thenReturn(testBrandResponse);

		/**
		 * Act
		 */
		ResponseDataModel<?> response = brandService.getList(page, keyword, false);

		/**
		 * Assert
		 */
		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Success", response.getResponseMsg());

		PageResponse<?> pageData = (PageResponse<?>) response.getData();
		assertNotNull(pageData);
		assertEquals(1, pageData.getTotalPages());
		assertEquals(1, pageData.getContent().size());

		verify(brandRepository, times(1)).findByBrandNameContainingIgnoreCase(eq(keyword), any(Pageable.class));
		verify(brandMapper, times(1)).toResponse(testBrand);
	}

	@Test
	@DisplayName("Should return empty list when no brands found")
	void testGetList_EmptyResult() {
		Page<BrandEntity> emptyPage = new PageImpl<>(new ArrayList<>(), org.springframework.data.domain.PageRequest.of(0, 5), 0);

		when(brandRepository.findByBrandNameContainingIgnoreCase(anyString(), any(Pageable.class)))
			.thenReturn(emptyPage);

		ResponseDataModel<?> response = brandService.getList(1, "", false);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());

		PageResponse<?> pageData = (PageResponse<?>) response.getData();
		assertEquals(0, pageData.getContent().size());
	}

	@Test
	@DisplayName("Should get brand by ID")
	void testGetById_Success() {
		when(brandRepository.findById(1)).thenReturn(Optional.of(testBrand));

		ResponseDataModel<?> response = brandService.getById(1);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Success", response.getResponseMsg());

		BrandResponse data = (BrandResponse) response.getData();
		assertNotNull(data);
		assertEquals(1, data.getBrandId());
		assertEquals("Apple", data.getBrandName());

		verify(brandRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("Should return not found when brand ID doesn't exist")
	void testGetById_NotFound() {
		when(brandRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = brandService.getById(999);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Brand not found", response.getResponseMsg());
		assertNull(response.getData());

		verify(brandRepository, times(1)).findById(999);
	}

	@Test
	@DisplayName("Should add brand successfully")
	void testAdd_Success() {
		when(brandRepository.existsByBrandName("Apple")).thenReturn(false);
		when(brandMapper.toEntity(testBrandRequest)).thenReturn(testBrand);
		when(brandRepository.saveAndFlush(testBrand)).thenReturn(testBrand);
		when(brandMapper.toResponse(testBrand)).thenReturn(testBrandResponse);

		ResponseDataModel<?> response = brandService.add(testBrandRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Brand is added successfully", response.getResponseMsg());

		BrandResponse data = (BrandResponse) response.getData();
		assertNotNull(data);
		assertEquals("Apple", data.getBrandName());

		verify(brandRepository, times(1)).existsByBrandName("Apple");
		verify(brandMapper, times(1)).toEntity(testBrandRequest);
		verify(brandRepository, times(1)).saveAndFlush(testBrand);
	}

	@Test
	@DisplayName("Should return duplicate error when brand name exists")
	void testAdd_DuplicateName() {
		when(brandRepository.existsByBrandName("Apple")).thenReturn(true);

		ResponseDataModel<?> response = brandService.add(testBrandRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_DUPL, response.getResponseCode());
		assertEquals("Brand name already exists", response.getResponseMsg());

		verify(brandRepository, times(1)).existsByBrandName("Apple");
		verify(brandMapper, never()).toEntity(any());
		verify(brandRepository, never()).saveAndFlush(any());
	}

	@Test
	@DisplayName("Should update brand successfully")
	void testUpdate_Success() {
		when(brandRepository.findById(1)).thenReturn(Optional.of(testBrand));
//		when(brandRepository.existsByBrandName("Apple")).thenReturn(false);
		doNothing().when(brandMapper).updateEntity(testBrandRequest, testBrand);
		when(brandRepository.saveAndFlush(testBrand)).thenReturn(testBrand);
		when(brandMapper.toResponse(testBrand)).thenReturn(testBrandResponse);

		ResponseDataModel<?> response = brandService.update(1, testBrandRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
		assertEquals("Brand is updated successfully", response.getResponseMsg());

		verify(brandRepository, times(1)).findById(1);
		verify(brandMapper, times(1)).updateEntity(testBrandRequest, testBrand);
		verify(brandRepository, times(1)).saveAndFlush(testBrand);
	}

	@Test
	@DisplayName("Should return not found when updating non-existent brand")
	void testUpdate_NotFound() {
		when(brandRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = brandService.update(999, testBrandRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Brand not found", response.getResponseMsg());

		verify(brandRepository, times(1)).findById(999);
		verify(brandMapper, never()).updateEntity(any(), any());
	}

	@Test
	@DisplayName("Should return duplicate error when updating with existing name")
	void testUpdate_DuplicateName() {
		BrandEntity existingBrand = new BrandEntity();
		existingBrand.setBrandId(1);
		existingBrand.setBrandName("Samsung");

		BrandRequest updateRequest = new BrandRequest();
		updateRequest.setBrandName("Apple");

		when(brandRepository.findById(1)).thenReturn(Optional.of(existingBrand));
		when(brandRepository.existsByBrandName("Apple")).thenReturn(true);

		ResponseDataModel<?> response = brandService.update(1, updateRequest);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_DUPL, response.getResponseCode());
		assertEquals("Brand name already exists", response.getResponseMsg());

		verify(brandRepository, times(1)).findById(1);
		verify(brandMapper, never()).updateEntity(any(), any());
	}

	@Test
	@DisplayName("Should delete brand successfully")
	void testDelete_Success() {
		BrandEntity brandWithProducts = new BrandEntity();
		brandWithProducts.setBrandId(1);
		brandWithProducts.setBrandName("Apple");
		brandWithProducts.setLogo("images/brand/apple.png");

		ProductEntity product = new ProductEntity();
		product.setProductId(1);
		product.setImage("images/product/iphone.jpg");

		brandWithProducts.setProducts(List.of(product));

		when(brandRepository.findById(1)).thenReturn(Optional.of(brandWithProducts));
		doNothing().when(brandRepository).delete(brandWithProducts);
		doNothing().when(brandRepository).flush();

		/**
		 * Mock static method
		 */
		try (var mockStatic = mockStatic(FileHelper.class)) {
			mockStatic.when(() -> FileHelper.deleteFile(anyString())).then(invocation -> null);

			ResponseDataModel<?> response = brandService.delete(1);

			assertNotNull(response);
			assertEquals(Constants.RESULT_CD_SUCCESS, response.getResponseCode());
			assertEquals("Brand and products in brand is deleted successfully", response.getResponseMsg());

			verify(brandRepository, times(1)).findById(1);
			verify(brandRepository, times(1)).delete(brandWithProducts);
			verify(brandRepository, times(1)).flush();
		}
	}

	@Test
	@DisplayName("Should return not found when deleting non-existent brand")
	void testDelete_NotFound() {
		when(brandRepository.findById(999)).thenReturn(Optional.empty());

		ResponseDataModel<?> response = brandService.delete(999);

		assertNotNull(response);
		assertEquals(Constants.RESULT_CD_NOT_FOUND, response.getResponseCode());
		assertEquals("Brand not found", response.getResponseMsg());

		verify(brandRepository, times(1)).findById(999);
		verify(brandRepository, never()).delete(any());
	}
}