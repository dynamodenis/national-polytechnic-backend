package com.mabawa.nnpdairy.services;

import com.google.gson.Gson;
import com.mabawa.nnpdairy.models.Products;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.repository.ProductsRepository;
import com.mabawa.nnpdairy.services.mongo.PImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductsService {
    @Autowired
    private Gson gson;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PImagesService pImagesService;

    public List<Products> getAllProducts(){
        List<Products> productsList = productsRepository.findAll();
        productsList.forEach(products -> {
            String prodId = products.getId().toString();
            products.setpImages(pImagesService.getProductImage(prodId));
        });
        return productsList;
    }

    public List<Products> getAllProductswithImages(){
        List<Products> productsList = productsRepository.findAll();
        productsList.forEach(products -> {
            products.setImageList(pImagesService.getProductImageList(products.getId().toString()));
        });
        return productsList;
    }

    public List<Products> filterProductsByType(Integer type, Pageable pageable){
        List<Products> productsList = productsRepository.filterStockByType(type, pageable);
        productsList.forEach(products -> {
            products.setImageList(pImagesService.getProductImageList(products.getId().toString()));
        });
        return productsList;
    }

    public List<Products> filterProductsByTypeAndCategory(Integer type, UUID category, Pageable pageable){
        List<Products> productsList = productsRepository.filterStockByTypeAndCategory(type, category, pageable);
        productsList.forEach(products -> {
            products.setImageList(pImagesService.getProductImageList(products.getId().toString()));
        });
        return productsList;
    }

    public Optional<Products> getProductByName(String name){
        return  productsRepository.findByNameIgnoreCase(name);
    }

    public Optional<Products> getProductById(UUID id){
        return productsRepository.findById(id);
    }

    public List<Products> getProductByNameContaining(String name){
        return productsRepository.findProductsByNameContaining(name);
    }

    public List<Products> filterProductByName(String name, Pageable pageable){
        return productsRepository.getStockByNameLike(name, pageable);
    }

    public Products create(Products products){
        return productsRepository.saveAndFlush(products);
    }

    public Products update(Products products){
        return productsRepository.save(products);
    }

    public void deleteProductById(UUID id){
        productsRepository.deleteProductsById(id);
    }

    public void deleteAllProducts(){
        productsRepository.deleteAllProducts();
    }

    public Products getJson(String product){
        Products products = gson.fromJson(product, Products.class);

        return products;
    }

}
