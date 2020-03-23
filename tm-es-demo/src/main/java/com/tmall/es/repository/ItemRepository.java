package com.tmall.es.repository;

import com.tmall.es.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    List<Item> findByPrice(Double begin,Double end);

}
