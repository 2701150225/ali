package com.ali.item.mapper;

import com.ali.item.pojo.SpecParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/19 16:17
 */
public interface SpecParamMapper extends Mapper<SpecParam> {
    @Delete("DELETE FROM tb_spec_param WHERE  id=#{id}")
    void deleteSpecParam(Long id);

    @Update("<script> UPDATE tb_spec_param SET `name` = #{name}  <if test='numeric!=null'> ,`numeric`=#{numeric} </if> <if test='unit!=null'> ,`unit`=#{unit} </if> <if test='generic!=null'>, `generic`=#{generic} </if> <if test='searching!=null'> ,`searching`=#{searching} </if> <if test='segments!=null'> ,`segments`=#{segments} </if> WHERE id=#{id}  </script>")
    void editSpecParam(SpecParam specParam);
}
