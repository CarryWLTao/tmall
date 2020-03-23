package com.tmall.es.demo;

import com.tmall.EsApplication;
import com.tmall.es.pojo.Item;
import com.tmall.es.repository.ItemRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class EsTest {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ItemRepository repository;

    @Test
    public void testCreate() {
        //创建索引库
        elasticsearchTemplate.createIndex(Item.class);
        //映射关系
        elasticsearchTemplate.putMapping(Item.class);
    }

    @Test
    public void insertIndex() {
        List<Item> list = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        item.setTitle("小米手机");
        item.setCategory("手机");
        item.setBrand("小米");
        item.setPrice(3699.00);
        item.setImages("http://image.leyou.com/3.jpg");
        Item item1 = new Item();
        item1.setId(2L);
        item1.setTitle("小米1手机");
        item1.setCategory("手1机");
        item1.setBrand("小1米");
        item1.setPrice(3629.00);
        item1.setImages("http://image.leyou.com/2.jpg");
        list.add(item);
        list.add(item1);
        // 接收对象集合，实现批量新增
        repository.saveAll(list);
    }

    @Test
    public void testFind() {
        Iterable<Item> all = repository.findAll();
        for (Item item : all) {
            System.out.println("item = " + item);
        }
    }

    @Test
    public void testFindBy() {
        List<Item> byPriceBetWeen = repository.findByPrice(2000d, 4000d);
        for (Item item : byPriceBetWeen) {
            System.out.println("价格在2000-4000的手机" + item);
        }
    }

    @Test
    public void testQuery() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "title", "price"}, null));
        //查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "小米手机"));
        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //分页
        queryBuilder.withPageable(PageRequest.of(0, 2));
        Page<Item> result = repository.search(queryBuilder.build());
        long totalElements = result.getTotalElements();
        System.out.println("totalElements = " + totalElements);
        int totalPages = result.getTotalPages();
        System.out.println("totalPages = " + totalPages);
        List<Item> list = result.getContent();
        for (Item item : list) {
            System.out.println("item = " + item);
        }

    }

    @Test
    public void testAgg() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        String aggName = "popularBrand";
        //聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(aggName).field("brand"));

        //查询并返回带聚合结果
        AggregatedPage<Item> result = elasticsearchTemplate.queryForPage(queryBuilder.build(), Item.class);

        //解析聚合
        Aggregations aggregations = result.getAggregations();

        //获取指定名称的聚合
        StringTerms terms = aggregations.get(aggName);

        //获取桶
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket: buckets){
            System.out.println("key = " + bucket.getKeyAsString());
            System.out.println("docCount = " + bucket.getDocCount());
        }

    }
}
