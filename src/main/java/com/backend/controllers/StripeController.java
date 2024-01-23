package com.backend.controllers;

import com.backend.request.ProductCheckoutRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection.AllowedCountry;
import com.stripe.param.checkout.SessionCreateParams.ShippingOption.ShippingRateData;
import com.stripe.param.checkout.SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate;
import com.stripe.param.checkout.SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/checkout")
public class StripeController {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostMapping
    public ResponseEntity<?> buyProducts(@RequestBody List<ProductCheckoutRequest > products) throws StripeException {
        Stripe.apiKey = apiKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl("http://localhost:4200/checkout-test/success")
                .setCancelUrl("http://localhost:4200/checkout-test/cancel")
                .addPaymentMethodType(PaymentMethodType.CARD)
//                .addPaymentMethodType(PaymentMethodType.PAYPAL)
//                .addPaymentMethodType(PaymentMethodType.WECHAT_PAY)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setShippingAddressCollection(ShippingAddressCollection.builder()
                        .addAllowedCountry(AllowedCountry.PE)
                        .addAllowedCountry(AllowedCountry.US)
                        .addAllowedCountry(AllowedCountry.MX)
                        .build())
                .addShippingOption(SessionCreateParams.ShippingOption.builder()
                        .setShippingRateData(ShippingRateData.builder()
                                .setType(ShippingRateData.Type.FIXED_AMOUNT)
                                .setFixedAmount(FixedAmount.builder()
                                        .setAmount(0L)
                                        .setCurrency("usd")
                                        .build())
                                .setDisplayName("Compra gratis")
                                .setDeliveryEstimate(DeliveryEstimate.builder()
                                        .setMinimum(DeliveryEstimate.Minimum.builder()
                                                .setUnit(DeliveryEstimate.Minimum.Unit.BUSINESS_DAY)
                                                .setValue(5L)
                                                .build())
                                        .setMaximum(DeliveryEstimate.Maximum.builder()
                                                .setUnit(DeliveryEstimate.Maximum.Unit.BUSINESS_DAY)
                                                .setValue(7L)
                                                .build())
                                        .build())
                                .build())
                        .build())
                .addAllLineItem(products.stream().map(product -> LineItem.builder()
                        .setPriceData(PriceData.builder()
                                .setCurrency("usd")
                                .setProductData(ProductData.builder()
                                        .setName(product.getName())
                                        .addImage(product.getImageUrl())
                                        .build())
                                .setUnitAmount(product.getPrice() * 100)
                                .build())
                        .setQuantity(product.getQuantity().longValue())
                        .build()).collect(Collectors.toList()))
                .build();
        Session session = Session.create(params);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("id", session.getId());
        return ResponseEntity.ok(hm);
    }
}
