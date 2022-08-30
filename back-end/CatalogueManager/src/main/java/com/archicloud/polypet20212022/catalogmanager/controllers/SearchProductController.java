package com.archicloud.polypet20212022.catalogmanager.controllers;

import com.archicloud.polypet20212022.catalogmanager.services.ResearchInterceptor;
import com.archicloud.polypet20212022.catalogmanager.services.SearchProductWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchProductController {

    private static final Logger log = LoggerFactory.getLogger(SearchProductController.class.getName());

    @Autowired
    private SearchProductWS searchProductWS;

    @Autowired
    private ResearchInterceptor researchInterceptor;

    @GetMapping("/search/{productName}")
    private SearchProductWS.ProductDTOs findProductByName(@PathVariable("productName") String productName) {
        try {
            var t = searchProductWS.findProductByName(productName);

            log.info(String.valueOf(t));
            return t;
        } catch (Exception e) {
            log.warn("e.getMessage()");
            e.printStackTrace();
            return new SearchProductWS.ProductDTOs(List.of());
        }
    }

    @GetMapping("/search/reference/{productReference}")
    public SearchProductWS.ProductDTO findProductByReference(@PathVariable("productReference") String reference) {
        try {
            log.info("\nFinding product of reference : " + reference + " ...");
            SearchProductWS.ProductDTO value = searchProductWS.findProductByReference(reference);
            researchInterceptor.incrementConsulting(reference, value);
            return value;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @GetMapping("/search/category/{category}")
    private SearchProductWS.ProductDTOs findProductByCategory(@PathVariable("category") String category) {
        try {
            return searchProductWS.findProductByCategory(category);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new SearchProductWS.ProductDTOs(List.of());
        }
    }
}
