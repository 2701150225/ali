package com.ali.item.mapper;

import com.ali.item.pojo.Brand;

import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/13 19:52
 */


public interface BrandMapper extends Mapper<Brand> {

    /**
     * 创建品牌与商品分类的关联表
     *
     * @param cid
     * @param bid
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES(#{cid}, #{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT b.* FROM tb_brand b INNER JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> selectBrandByCid(Long cid);


    @Update("UPDATE tb_brand SET tb_brand.`name` = #{name} ,image = #{image}, letter =#{letter} WHERE id=#{id}")
    void updateBrand(Brand brand);

    /**
     * 更新品牌与商品分类的关联表
     * @param cid
     * @param id
     */
    @Update("UPDATE tb_category_brand SET category_id=#{cid} WHERE brand_id=#{id}")
    void updateBrandAndCategory(@Param("cid") Long cid,@Param("id") Long id);

    @Delete("DELETE FROM  tb_brand WHERE id=#{bid} ")
    void deleteById(Long bid);

    @Delete("DELETE FROM  tb_category_brand WHERE brand_id=#{bid}")
    void deleteBrandAndCategory(Long bid);
}
