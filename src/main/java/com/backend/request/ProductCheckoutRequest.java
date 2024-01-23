package com.backend.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCheckoutRequest {
    private String name;
    private String imageUrl;
    private Long price;
    private Integer quantity;
}
