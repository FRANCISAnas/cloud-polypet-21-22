package com.archicloud.polypet20212022.catalogmanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.archicloud.polypet20212022.catalogmanager.components.StorehouseRepositories;
import com.archicloud.polypet20212022.catalogmanager.entity.Address;
import com.archicloud.polypet20212022.catalogmanager.entity.InternalProduct;
import com.archicloud.polypet20212022.catalogmanager.entity.PartnerProduct;
import com.archicloud.polypet20212022.catalogmanager.entity.Product;
import com.archicloud.polypet20212022.catalogmanager.entity.ProductInventory;
import com.archicloud.polypet20212022.catalogmanager.entity.StoreHouse;
import com.archicloud.polypet20212022.catalogmanager.repositories.InternalProductRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.PartnerProductRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.PartnerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchProductWS {
    private static final Logger log = LoggerFactory.getLogger(SearchProductWS.class.getName());

    @Autowired
    private InternalProductRepository internalRepository;

    @Autowired
    private PartnerProductRepository partnerProductRepository;

    @Autowired
    private StorehouseRepositories storehouseRepositories;

    @Autowired
    private PartnerRepository partnerRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public SearchProductWS(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ProductDTO findProductByReference(String productReference) throws Exception {
        return findProductByReference(productReference, false);
    }

    public ProductDTO findProductByReference(String productReference, boolean askToPartner) throws Exception {

        var productOpt = internalRepository.existsByReference(productReference)
                ? internalRepository.findByReference(productReference)
                : partnerProductRepository.findByReference(productReference);
        if (productOpt.isPresent()) {
            var product = productOpt.get();

            Address address = null;
            Optional<StoreHouse> warehousesOpt = storehouseRepositories
                    .findByProductInventories(new ProductInventory(product, 0));

            if (product.getOrigin().contains(InternalProduct.JolyPetLocalName)) {

                if (warehousesOpt.isPresent())
                    address = warehousesOpt.get().getAddress();
                return new ProductDTO(product, address);
            } else {

                if (!askToPartner)
                    return new ProductDTO(product, null);

                var partnerOpt = partnerRepository.findByName(product.getOrigin());

                if (partnerOpt.isPresent()) {

                    try {
                        log.info("Asking address to external partner.");

                        var partnerAddressResponse = this.restTemplate.getForEntity(
                                partnerOpt.get().getInventoryUrl() + "/j/warehouse/product/address/" + productReference,
                                Address.class);
                        if (partnerAddressResponse.getStatusCode() == HttpStatus.OK) {
                            address = partnerAddressResponse.getBody();
                        } else
                            log.warn("Partner don't have the address of product " + productReference);
                    } catch (RestClientException e) {
                        log.info("Post request for get product store address...");
                        // e.printStackTrace();
                    }
                }

                return new ProductDTO(product, address);
            }
        }

        throw new Exception("product doesn't exist !");

    }

    public ProductDTOs findProductByCategory(String category) throws Exception {
        var products = new ProductDTOs(new ArrayList<>());
        var internalProducts = internalRepository.findAllByCategory(category);
        getInternalProductDTOS(products, internalProducts);

        var partnerProducts = partnerProductRepository.findAllByCategory(category);
        return getPartnerProductDTOS(category, products, partnerProducts);

    }

    public ProductDTOs findProductByName(String productName) throws Exception {
        var products = new ProductDTOs(new ArrayList<>());
        var internalProducts = internalRepository.findByNameContaining(productName);
        getInternalProductDTOS(products, internalProducts);
        var partnerProducts = partnerProductRepository.findByNameContaining(productName);
        return getPartnerProductDTOS(productName, products, partnerProducts);

    }

    private void getInternalProductDTOS(ProductDTOs products, List<InternalProduct> internalProducts) {
        Optional<StoreHouse> warehousesOpt;
        Address address = null;
        if (internalProducts != null && !internalProducts.isEmpty())
            for (InternalProduct internalProduct : internalProducts) {
                warehousesOpt = storehouseRepositories
                        .findByProductInventories(new ProductInventory(internalProduct, 0));
                if (warehousesOpt.isPresent())
                    address = warehousesOpt.get().getAddress();
                products.productDTOS.add(new ProductDTO(internalProduct, address));
            }
    }

    private ProductDTOs getPartnerProductDTOS(String productName, ProductDTOs products,
            List<PartnerProduct> partnerProducts) throws Exception {
        Optional<StoreHouse> warehousesOpt;
        log.info(String.valueOf(partnerProducts));
        if (partnerProducts != null && !partnerProducts.isEmpty())
            for (PartnerProduct partnerProduct : partnerProducts) {
                Address address = null;
                warehousesOpt = storehouseRepositories
                        .findByProductInventories(new ProductInventory(partnerProduct, 0));
                if (warehousesOpt.isEmpty()) {
                    var partnerOpt = partnerRepository.findByName(partnerProduct.getOrigin());

                    if (partnerOpt.isPresent()) {
                        try {
                            log.info(String.format("Asking infos to external partner %s", partnerProduct.getOrigin()));

                            var partnerAddressResponse = this.restTemplate
                                    .getForEntity(partnerOpt.get().getInventoryUrl() + "/j/warehouse/product/address/"
                                            + partnerProduct.getReference(), Address.class);
                            if (partnerAddressResponse.getStatusCode() == HttpStatus.OK) {
                                address = partnerAddressResponse.getBody();
                            } else {
                                log.warn("Partner don't get the address of product " + partnerProduct.getReference());
                            }
                        } catch (RestClientException e) {
                            log.info("Post request for get product store address...");
                            // e.printStackTrace();
                        }
                    } else
                        log.info(String.format("Partner %s not found", partnerProduct.getOrigin()));
                } else
                    address = warehousesOpt.get().getAddress();

                products.productDTOS.add(new ProductDTO(partnerProduct, address));
            }

        if (!products.productDTOS.isEmpty()) {
            return products;
        }

        throw new Exception(String.format("No product with category %s !", productName));
    }

    public record ProductDTO(Product product, Address address) {

    }

    public record ProductDTOs(List<ProductDTO> productDTOS) {

    }
}