package com.ali.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "tb_spec_group")
@Data
public class SpecGroup  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    //表示该属性并非一个到数据库表的字段的映射
    private List<SpecParam> params;


}