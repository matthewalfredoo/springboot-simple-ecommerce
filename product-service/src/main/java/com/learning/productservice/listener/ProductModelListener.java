package com.learning.productservice.listener;

import com.learning.productservice.entity.Product;
import com.learning.productservice.service.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductModelListener extends AbstractMongoEventListener<Product> {

    private SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Product> event) {
        // this will check if the id sent from the client is null or less than 1
        // if it is, then it will generate a new id for the product
        // mostly, clients will not send the id from the request body
        if(event.getSource().getId() == null || event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
        }
    }

}
