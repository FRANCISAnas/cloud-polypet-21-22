package com.archicloud.polypet20212022.catalogmanager.services;

import com.archicloud.polypet20212022.catalogmanager.components.StorehouseRepositories;
import com.archicloud.polypet20212022.catalogmanager.entity.*;
import com.archicloud.polypet20212022.catalogmanager.repositories.InternalProductRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.PartnerProductRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.PartnerRepository;
import com.archicloud.polypet20212022.catalogmanager.repositories.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InventoryWs {
    private static final Logger log = LoggerFactory.getLogger(InventoryWs.class.getName());

    PartnerProductRepository partnerProductRepository;
    InternalProductRepository internalProductRepository;
    PartnerRepository partnerRepository;
    SearchProductWS searchProductWS;
    WarehouseRepository warehouseRepository;
    StorehouseRepositories storehouseRepositories;

    private final RestTemplate restTemplate;

    @Autowired
    public InventoryWs(InternalProductRepository internalProductRepository,
            PartnerProductRepository partnerProductRepository, PartnerRepository partnerRepository,
            SearchProductWS searchProductWS, WarehouseRepository warehouseRepository,
            StorehouseRepositories storehouseRepositories, RestTemplateBuilder restTemplateBuilder) {
        this.internalProductRepository = internalProductRepository;
        this.partnerProductRepository = partnerProductRepository;
        this.partnerRepository = partnerRepository;
        this.searchProductWS = searchProductWS;
        this.restTemplate = restTemplateBuilder.build();
        this.warehouseRepository = warehouseRepository;
        this.storehouseRepositories = storehouseRepositories;
        var internalProductOpt = internalProductRepository.findByReference("JolyPet_base_name_Laisse");
        if (internalProductOpt.isEmpty()) {
            InternalProduct internalProduct = new InternalProduct();
            internalProduct.setName("Laisse");
            internalProduct.setCategory("Caniche");
            internalProduct.setPrice(105.0);
            internalProductRepository.save(internalProduct);
        }
    }

    public PartnerProduct updateProduct(PartnerProduct partnerProduct) {
        return partnerProductRepository.save(partnerProduct);
    }

    public List<PartnerProduct> updateProduct(List<PartnerProduct> partnerProducts) {
        var savedPartnerProducts = new ArrayList<PartnerProduct>();
        partnerProducts
                .forEach(partnerProduct -> savedPartnerProducts.add(partnerProductRepository.save(partnerProduct)));
        return savedPartnerProducts;
    }

    public InternalProduct updateProduct(InternalProduct internalProduct) {
        return internalProductRepository.save(internalProduct);
    }

    public Long createStore(String name, Address address) {
        return warehouseRepository.save(new StoreHouse(name, address)).getId();
    }

    public Integer storeProduct(String reference, Long storeId, Integer quantity) {
        var storehouseDTO = warehouseRepository.findById(storeId);

        if (storehouseDTO.isPresent()) {
            try {
                var productDTO = searchProductWS.findProductByReference(reference, false);
                var storehouse = storehouseDTO.get();
                var newQuantity = storehouse.incrementProductQuantity(productDTO.product(), quantity);
                if (newQuantity == -1)
                    log.warn(String.format("Something went wrong while incrementing %s at the store %s !",
                            productDTO.product().getName(), storehouseDTO.get().getName()));
                else
                    warehouseRepository.save(storehouse);
                return newQuantity;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info(String.format("StoreHouse  %s not found.", storeId));
        return -1;
    }

    public boolean bookCart(List<ProductInventory> orderLines) {
        for (ProductInventory orderLine : orderLines) {
            if (orderLine != null) {
                var product = orderLine.getProduct();
                if (product.getOrigin() != null && !product.getOrigin().contains(InternalProduct.JolyPetLocalName)) {
                    // External Partner

                    var partnerOpt = partnerRepository.findByName(product.getOrigin());

                    if (partnerOpt.isPresent()) {

                        try {
                            log.info(String.format("Booking product %s from external partner %s at %s...",
                                    product.getName(), product.getOrigin(),
                                    partnerOpt.get().getInventoryUrl() + "/j/purchase/book/product/"));

                            var partnerAddressResponse = this.restTemplate
                                    .getForEntity(partnerOpt.get().getInventoryUrl() + "/j/purchase/book/product/"
                                            + product.getReference(), String.class);
                            if (partnerAddressResponse.getStatusCode() != HttpStatus.OK
                                    || Boolean.FALSE.toString().equals(partnerAddressResponse.getBody())) {
                                log.warn(String.format("Sorry product %s from external partner %s can't be booked!",
                                        product.getName(), product.getOrigin()));
                                return false;
                            }
                        } catch (RestClientException e) {
                            log.warn(String.format("Partner %s at %s can't be reached!", product.getOrigin(),
                                    partnerOpt.get().getInventoryUrl()));
                            e.printStackTrace();
                            return false;
                        }
                    }
                } else {
                    log.info(String.format("Booking product %s in local...", product.getName()));
                    var storeHouseOptional = storehouseRepositories.findByProductInventories(orderLine.getReference());
                    if (storeHouseOptional.isPresent()) {
                        var productStoredQuantity = storeHouseOptional.get()
                                .getProductQuantity(orderLine.getReference());
                        if (productStoredQuantity < orderLine.getQuantity()) {
                            log.info(String.format(
                                    "Sorry the product %s with reference %s stock is less than %s, current quantity is %s",
                                    orderLine.getProduct().getName(), orderLine.getReference(), orderLine.getQuantity(),
                                    productStoredQuantity));
                            return false;
                        }
                    } else {
                        log.info(String.format("Sorry the product %s with reference %s has any stock.",
                                orderLine.getProduct().getName(), orderLine.getReference()));
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public boolean validateCart(List<ProductInventory> orderLines) {
        for (ProductInventory orderLine : orderLines) {
            if (orderLine != null) {
                var product = orderLine.getProduct();
                if (product.getOrigin() != null && !product.getOrigin().contains(InternalProduct.JolyPetLocalName)) {
                    // External Partner

                    var partnerOpt = partnerRepository.findByName(product.getOrigin());

                    if (partnerOpt.isPresent()) {

                        try {
                            log.info(String.format("Purchasing %s product %s bought from external partner %s at %s...",
                                    orderLine.getQuantity(), product.getName(), product.getOrigin(),
                                    partnerOpt.get().getInventoryUrl()
                                            + "/j/purchase/purchase/product/<reference>/<quantity>"));

                            var partnerAddressResponse = this.restTemplate.getForEntity(
                                    partnerOpt.get().getInventoryUrl() + "/j/purchase/product/{reference}/{quantity}",
                                    String.class, product.getReference(), orderLine.getQuantity());
                            if (partnerAddressResponse.getStatusCode() != HttpStatus.OK
                                    || Boolean.FALSE.toString().equals(partnerAddressResponse.getBody())) {
                                log.warn(String.format("Sorry product %s from external partner %s can't be purchased!",
                                        product.getName(), product.getOrigin()));
                                return false;
                            }
                        } catch (RestClientException e) {
                            log.warn(String.format("Partner %s at %s can't be reached!", product.getOrigin(),
                                    partnerOpt.get().getInventoryUrl()));
                            e.printStackTrace();
                            return false;
                        }
                    }
                } else {
                    log.info(String.format("Purchasing product %s in local...", product.getName()));

                    var storeHouseOptional = storehouseRepositories.findByProductInventories(orderLine.getReference());
                    if (storeHouseOptional.isPresent()) {
                        var productStoredQuantity = storeHouseOptional.get()
                                .getProductQuantity(orderLine.getReference());
                        if (productStoredQuantity < orderLine.getQuantity()) {
                            log.info(String.format(
                                    "Sorry the product %s with reference %s stock is less than %s, current quantity is %s",
                                    orderLine.getProduct().getName(), orderLine.getReference(), orderLine.getQuantity(),
                                    productStoredQuantity));
                            return false;
                        } else {
                            var storehouse = storeHouseOptional.get();
                            var newQuantity = storehouse.decrementProductQuantity(product, orderLine.getQuantity());
                            if (newQuantity == -1)
                                log.warn(String.format("Something went wrong while decrementing %s at the store %s !",
                                        product.getName(), storehouse.getName()));
                            else
                                warehouseRepository.save(storehouse);
                        }
                    } else {
                        log.info(String.format("Sorry the product %s with reference %s has any stock.",
                                orderLine.getProduct().getName(), orderLine.getReference()));
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public Long getAvailableInternalStore() {
        var warehouses = warehouseRepository.findAll();
        return warehouses.size() > 0 ? warehouses.get(0).getId() : (long) -1;
    }

    public Integer getQuantity(String reference) {
        try {
            var productDTO = searchProductWS.findProductByReference(reference, false);
            var product = productDTO.product();
            assert product != null;
            if (product.getOrigin() != null && !product.getOrigin().contains(InternalProduct.JolyPetLocalName)) {
                // External Partner

                var partnerOpt = partnerRepository.findByName(product.getOrigin());

                if (partnerOpt.isPresent()) {

                    try {
                        log.info(
                                String.format("Getting product %s available quantity from external partner %s at %s...",
                                        product.getName(), product.getOrigin(),
                                        partnerOpt.get().getInventoryUrl() + "/j/purchase/book/product/"));

                        var partnerProductQuantityResponse = this.restTemplate.getForEntity(
                                partnerOpt.get().getInventoryUrl() + "/j/product/{reference}/quantity", String.class,
                                product.getReference());
                        if (partnerProductQuantityResponse.getStatusCode() != HttpStatus.OK) {
                            log.warn(String.format(
                                    "Sorry product %s from external partner %s doesn't have any quantity!",
                                    product.getName(), product.getOrigin()));
                            return -1;
                        } else {
                            try {
                                return Integer
                                        .parseInt(Objects.requireNonNull(partnerProductQuantityResponse.getBody()));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                log.warn(String.format(
                                        "Sorry product %s from external partner %s quantity is not correct (%s) !",
                                        product.getName(), product.getOrigin(),
                                        partnerProductQuantityResponse.getBody()));
                                return -1;
                            }

                        }
                    } catch (RestClientException e) {
                        log.warn(String.format("Partner %s at %s can't be reached!", product.getOrigin(),
                                partnerOpt.get().getInventoryUrl()));
                        e.printStackTrace();
                        return -1;
                    }
                }
            } else {
                log.info(String.format("Getting product %s available quantity in local...", product.getName()));

                var storeHouseOptional = storehouseRepositories.findByProductInventories(product.getReference());
                if (storeHouseOptional.isPresent()) {

                    var productStoredQuantity = storeHouseOptional.get().getProductQuantity(product.getReference());

                    if (productStoredQuantity == -1) {
                        log.info(String.format("Sorry the product %s with reference %s has any stock!",
                                product.getName(), product.getReference()));
                        return -1;
                    }
                    return productStoredQuantity;
                } else {
                    log.info(String.format("Sorry the product %s with reference %s has any stock.", product.getName(),
                            product.getReference()));
                    return -1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.warn(String.format("Sorry the product with reference %s was not found!", reference));
        }
        return -1;
    }
}