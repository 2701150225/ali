package com.ali.item.mapper;


import com.ali.item.pojo.SpecGroup;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;


/**
 * @Author:wangsusheng
 * @Date: 2020/1/19 16:17
 */
public interface SpecGroupMapper extends Mapper<SpecGroup> {


    @Update("UPDATE tb_spec_group SET  `name`=#{name}  WHERE  id=#{id}")
    void editSpecGroup(SpecGroup specGroup);

    @Delete("DELETE FROM tb_spec_group WHERE  id=#{id}")
    void deleteSpecGroup(Long id);
}
