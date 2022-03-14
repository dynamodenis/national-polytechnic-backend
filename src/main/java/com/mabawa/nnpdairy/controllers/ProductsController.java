package com.mabawa.nnpdairy.controllers;

import com.mabawa.nnpdairy.constants.Constants;
import com.mabawa.nnpdairy.models.Products;
import com.mabawa.nnpdairy.models.Response;
import com.mabawa.nnpdairy.models.mongo.PImages;
import com.mabawa.nnpdairy.services.ImageService;
import com.mabawa.nnpdairy.services.ProductsService;
import com.mabawa.nnpdairy.services.mongo.PImagesService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping({"api/v1/products"})
public class ProductsController {
    @Autowired
    private ProductsService productsService;

    @Autowired
    private PImagesService pImagesService;

    @Autowired
    private ImageService imageService;

    String title = Constants.TITLES[0];

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> addNewProduct(@RequestPart("product") String product, @RequestPart("image") MultipartFile image) {
        Products products = productsService.getJson(product);
        String msg = "";
        Optional<Products> productsOptional = productsService.getProductByName(products.getName());
        if (productsOptional.isPresent()){
            msg = "A Product exists By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            LocalDateTime lastLocal = LocalDateTime.now();
            products.setCreated(Timestamp.valueOf(lastLocal));

            products = productsService.create(products);

            List<PImages> pImagesList = new ArrayList<>();
            if (image != null && !image.isEmpty()){
                try{
                    PImages pImages = new PImages();
                    pImages.setProductId(products.getId().toString());
                    pImages.setpName(products.getName());
                    pImages.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                    String profStr = pImagesService.addPImage(pImages);
                    pImages.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(pImages.getImage().getData())));

                    pImagesList.add(pImages);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }

            products.setpImages(pImagesList);


            HashMap stockMap = new HashMap();
            stockMap.put("product", products);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], stockMap);
        }
    }

    @PutMapping(path = {"edit-product/{id}"}, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editProduct(@PathVariable UUID id, @RequestPart("product") String product, @RequestPart("image") MultipartFile image) {
        Products products = productsService.getJson(product);
        String msg = "";
        Optional<Products> productsOptional = productsService.getProductById(products.getId());
        if (!productsOptional.isPresent()){
            msg = "A Product doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            Products savedProd = productsOptional.get();
            products.setId(savedProd.getId());

            products = productsService.update(products);

            List<PImages> pImagesList = new ArrayList<>();

            if (image != null && !image.isEmpty()){
                try{
                    pImagesService.deleteProductImage(products.getId().toString());

                    PImages pImages = new PImages();
                    pImages.setProductId(products.getId().toString());
                    pImages.setpName(products.getName());
                    pImages.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                    String profStr = pImagesService.addPImage(pImages);

                    pImages.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(pImages.getImage().getData())));

                    pImagesList.add(pImages);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }else{
                pImagesList = pImagesService.getProductImage(products.getId().toString());
            }

            products.setpImages(pImagesList);

            HashMap stockMap = new HashMap();
            stockMap.put("product", products);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], stockMap);
        }
    }

    @PutMapping(path = {"edit-product-image/{id}"}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> editProductImage(@PathVariable UUID id, @RequestPart("image") MultipartFile image) {
        String msg = "";
        Optional<Products> productsOptional = productsService.getProductById(id);
        if (!productsOptional.isPresent()){
            msg = "A Product doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            Products savedProd = productsOptional.get();
            List<PImages> pImagesList = new ArrayList<>();

            if (image != null && !image.isEmpty()){
                try{
                    pImagesService.deleteProductImage(id.toString());

                    PImages pImages = new PImages();
                    pImages.setProductId(savedProd.getId().toString());
                    pImages.setpName(savedProd.getName());
                    pImages.setImage(new Binary(BsonBinarySubType.BINARY, imageService.compressBytes(image.getBytes())));

                    String profStr = pImagesService.addPImage(pImages);

                    pImages.setImageDownload(Base64.getEncoder().encodeToString(imageService.decompressBytes(pImages.getImage().getData())));

                    pImagesList.add(pImages);
                }catch (IOException ex){
                    System.out.printf("Error : " + ex.toString());
                }
            }else{
                pImagesList = pImagesService.getProductImage(savedProd.getId().toString());
            }

            savedProd.setpImages(pImagesList);

            HashMap stockMap = new HashMap();
            stockMap.put("product", savedProd);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[0], stockMap);
        }
    }

    @GetMapping
    public ResponseEntity<Response> getProductsList() {
        List<Products> productsList = productsService.getAllProducts();

        HashMap stockMap = new HashMap();
        stockMap.put("products", productsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
    }

    @GetMapping(path = "/with-images-list")
    public ResponseEntity<Response> getProductsWithImagesList() {
        List<Products> productsList = productsService.getAllProductswithImages();

        HashMap stockMap = new HashMap();
        stockMap.put("products", productsList);
        return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
    }

    @GetMapping(path = {"searchById/{id}"})
    public ResponseEntity<Response> getProductById(@PathVariable UUID id){
        String msg = "";
        Optional<Products> productsOptional = productsService.getProductById(id);
        if (!productsOptional.isPresent()){
            msg = "A Product doesn't exists By ID Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            Products products = productsOptional.get();
            List<PImages> pImagesList = pImagesService.getProductImage(products.getId().toString());
            products.setpImages(pImagesList);

            HashMap stockMap = new HashMap();
            stockMap.put("product", products);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
        }
    }

    @GetMapping(path = {"searchByName/{name}"})
    public ResponseEntity<Response> getProductByName(@PathVariable String name){
        String msg = "";
        Optional<Products> productsOptional = productsService.getProductByName(name);
        if (!productsOptional.isPresent()){
            msg = "A Product doesn't exists By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            Products products = productsOptional.get();
            List<PImages> pImagesList = pImagesService.getProductImage(products.getId().toString());
            products.setpImages(pImagesList);

            HashMap stockMap = new HashMap();
            stockMap.put("product", products);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
        }
    }

    @GetMapping(path = {"filter/{name}"})
    public ResponseEntity<Response> filterProductsByName(@PathVariable String name, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize){
        String msg = "";
        Pageable paging = PageRequest.of(pageNo, pageSize);
        List<Products> productsList = productsService.filterProductByName(name, paging);
        if (productsList == null || productsList.isEmpty()){
            msg = "Product(s) doesn't exists By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            productsList.forEach(products -> {
                String prodId = products.getId().toString();
                products.setpImages(pImagesService.getProductImage(prodId));
            });

            HashMap stockMap = new HashMap();
            stockMap.put("product", productsList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
        }
    }

    @GetMapping(path = {"filter-type/{type}"})
    public ResponseEntity<Response> filterProductsByType(@PathVariable Integer type, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize){
        String msg = "";
        Pageable paging = PageRequest.of(pageNo, pageSize);
        List<Products> productsList = productsService.filterProductsByType(type, paging);
        if (productsList == null || productsList.isEmpty()){
            msg = "Product(s) doesn't exists By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            productsList.forEach(products -> {
                String prodId = products.getId().toString();
                products.setpImages(pImagesService.getProductImage(prodId));
            });

            HashMap stockMap = new HashMap();
            stockMap.put("product", productsList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
        }
    }

    @GetMapping(path = {"filter-type-category/{type}/{categoryid}"})
    public ResponseEntity<Response> filterProductsByTypeandCategory(@PathVariable Integer type, @PathVariable UUID categoryid, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize){
        String msg = "";
        Pageable paging = PageRequest.of(pageNo, pageSize);
        List<Products> productsList = productsService.filterProductsByTypeAndCategory(type, categoryid, paging);
        if (productsList == null || productsList.isEmpty()){
            msg = "Product(s) doesn't exists By Name Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        }else{
            productsList.forEach(products -> {
                String prodId = products.getId().toString();
                products.setpImages(pImagesService.getProductImage(prodId));
            });

            HashMap stockMap = new HashMap();
            stockMap.put("product", productsList);
            return this.getResponseEntity(this.title, Constants.STATUS[0], 1, Constants.MESSAGES[3], stockMap);
        }
    }

    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity<Response> deleteProductById(@PathVariable UUID id) {
        Optional<Products> OptionalProduct = productsService.getProductById(id);
        if (!OptionalProduct.isPresent()) {
            String msg = "No Stock Item exists By Stock ID Provided.";
            return new ResponseEntity<Response>(this.PResponse(this.title, Constants.STATUS[2], 0, msg, new HashMap()), HttpStatus.BAD_REQUEST);
        } else {
            pImagesService.deleteProductImage(id.toString());
            productsService.deleteProductById(id);
            return this.getResponseEntity(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
        }
    }

    @DeleteMapping(path = {"/deletePImage/{id}"})
    public ResponseEntity<Response> deleteProductImageByPId(@PathVariable UUID id) {
        List<Products> productsList = productsService.getAllProducts();
        productsList.forEach(products -> {
            String prodId = products.getId().toString();
            pImagesService.deleteProductImage(prodId);
        });
        pImagesService.deleteProductImage(id.toString());

        return this.getResponseEntity(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    @DeleteMapping(path = {"/deleteAll"})
    public ResponseEntity<Response> deleteAllProducts() {
        productsService.deleteAllProducts();
        return this.getResponseEntity(Constants.TITLES[0], Constants.STATUS[0], 1, Constants.MESSAGES[4], new HashMap());
    }

    private ResponseEntity<Response> getResponseEntity(String title, String status, Integer success, String msg, HashMap map) {
        return new ResponseEntity<Response>(this.PResponse(title, status, success, msg, map), HttpStatus.OK);
    }

    private Response PResponse(String title, String status, Integer success, String msg, HashMap map) {
        Response response = new Response();
        response.setTitle(title);
        response.setStatus(status);
        response.setMessage(msg);
        response.setSuccess(success);
        response.setData(map);
        return response;
    }
}
