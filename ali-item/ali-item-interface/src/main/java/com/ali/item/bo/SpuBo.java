package com.ali.item.bo;

import com.ali.item.pojo.Sku;
import com.ali.item.pojo.Spu;
import com.ali.item.pojo.SpuDetail;
import lombok.Data;

import java.util.List;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/19 16:17
 */

@Data
public class SpuBo   extends Spu {
    private String cname;

    private String bname;

    private SpuDetail spuDetail;

    private List<Sku> skus;



}
