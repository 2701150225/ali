package com.ali.item.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "tb_spec_param")
@Data
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;

    //这里的"`numeric`"，表示numeric是mysql的一个关键词，如果在这里直接使用会报错，所以需要添加@Column注解标识，并使用``来包围
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;


}