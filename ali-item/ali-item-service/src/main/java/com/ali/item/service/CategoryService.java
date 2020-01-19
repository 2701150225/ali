package com.ali.item.service;

import com.ali.item.mapper.CategoryMapper;
import com.ali.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/13 19:52
 **/
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父类目id来查询子节点
     *
     * @param pid 父类目id
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid) {
        //创建一个分类对象，设置父类目id。用来查询
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    /**
     * 根据一个id集合来查询多个对象，并返回对象的名称集合
     *
     * @param ida
     * @return
     */
    public List<String> queryNamesByIds(List<Long> ida) {
        List<Category> categoryList = categoryMapper.selectByIdList(ida);
        //注意这个lambda表达式
        // category -> {return category.getName();} == category -> category.getName()
        return categoryList.stream().map(category -> category.getName()).collect(Collectors.toList());
    }


    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }
}
