package com.backend.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface ProductProjection {
    String getName();

    Long getId();
    String getImageUrl();

    String getDescription();

    BigDecimal getPrice();

    @JsonProperty("category")
    String getCategoryName();
}
