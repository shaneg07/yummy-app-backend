package com.shane.esdtask.mapper;

import com.shane.esdtask.dto.ProductReq;
import com.shane.esdtask.entity.product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public product toproduct(ProductReq request) {
        return product.builder()
                .price(request.price())
                .name(request.name())
                .build();

    }
}
