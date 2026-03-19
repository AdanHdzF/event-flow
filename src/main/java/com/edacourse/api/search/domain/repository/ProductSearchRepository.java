package com.edacourse.api.search.domain.repository;

import java.util.List;

import com.edacourse.api.search.domain.model.SearchResult;
import com.edacourse.api.search.domain.model.SearchableProduct;

public interface ProductSearchRepository {
	void index(SearchableProduct product);

	void delete(String productId);

	List<SearchResult> searchByText(String query, int limit);

	List<SearchResult> searchBySemantic(String query, int limit);

	List<SearchResult> searchHybrid(String query, int limit);
}
