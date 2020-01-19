package com.ali.item.service;

import com.ali.common.pojo.PageResult;
import com.ali.item.mapper.BrandMapper;
import com.ali.item.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/13 20:01
 */
@Service
public class BrandService {


    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {


        // 初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        List<Brand> brands = brandMapper.selectByExample(example);
        // 包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增品牌
     *
     * @param brand 品牌信息
     * @param cids  关联的商品分类
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void savebrand(Brand brand, List<Long> cids) {
        //先新增品牌brand
        this.brandMapper.insertSelective(brand);

        //再新增中间表
        cids.forEach(cid -> {
            this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });
    }

    /**
     * 根据分类id查询所有的品牌
     *
     * @param cid 分类id
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {
        return this.brandMapper.selectBrandByCid(cid);
    }

    /**
     * 根据品牌id查询品牌
     *
     * @param id
     * @return
     */
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }

    public void updatebrand(Brand brand, List<Long> cids) {
        //更新品牌
        this.brandMapper.updateBrand(brand);
        //更新中间表
        System.out.println(cids);
        cids.forEach(cid -> {
            brandMapper.updateBrandAndCategory(cid, brand.getId());

        });

    }

    /**
     * 删除品牌
     *
     * @param bid
     */
    public void deleteBrand(Long bid) {
        brandMapper.deleteById(bid);
        brandMapper.deleteBrandAndCategory(bid);

    }
}
