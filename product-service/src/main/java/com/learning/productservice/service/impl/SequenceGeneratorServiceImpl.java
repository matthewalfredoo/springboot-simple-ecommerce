package com.learning.productservice.service.impl;

import com.learning.productservice.entity.DatabaseSequence;
import com.learning.productservice.service.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@AllArgsConstructor
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private MongoOperations mongoOperations;

    @Override
    public Long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class
        );

        return counter.getSeq();
    }

}
