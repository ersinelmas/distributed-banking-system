package com.dbs.customer.domain.repository.elastic;

import com.dbs.customer.domain.model.elastic.CustomerIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerElasticRepository extends ElasticsearchRepository<CustomerIndex, String> {
    List<CustomerIndex> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}