package com.ali.item.service;

import com.ali.common.pojo.PageResult;
import com.ali.item.bo.SpuBo;
import com.ali.item.mapper.*;
import com.ali.item.pojo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/19 16:17
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    //消息中间件使用模版
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据条件分页查询spu
     *
     * @param key      查询关键词
     * @param saleable 查询类型：null:全部，true上架，false下架
     * @param page     当前页
     * @param rows     每页数量
     * @return
     */
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //添加查询条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //添加上下架条件
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //添加分页
        PageHelper.startPage(page, rows);

        //执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(spus);

        //spu集合转化成spuBo集合，使用lambda表达式来处理原集合，并返回一个新的集合
        List< SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

            //查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            //查询分类名称
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            //把集合分隔，使用"-"来连接
            spuBo.setCname(StringUtils.join(names, "-"));

            //这里的return代表着返回的新的集合是一个list<spuBo>类型的集合
            return spuBo;
        }).collect(Collectors.toList());

        //返回pageResult
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), spuBos);
    }

    /**
     * 新增商品
     *
     * @param spuBo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveGoods(SpuBo spuBo) {
        Date date = new Date();
        //补全spu的参数
        spuBo.setId(null); //自设置id，防止恶意注入
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(date);
        spuBo.setLastUpdateTime(date);

        //新增spu
        this.spuMapper.insertSelective(spuBo);

        //新增spu-detail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        this.saveSkuAndStock(spuBo);

        //发送消息
        sendMsg("insert", spuBo.getId());
    }

    /**
     * 根据spuId查询spuDetail
     *
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spuId查询sku集合
     *
     * @param spuId
     * @return
     */
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(sku);
        System.out.println(skus);
        //遍历集合，获取库存
        skus.forEach(sku1 -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 更新商品信息
     *
     * @param spuBo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(SpuBo spuBo) {
        Date date = new Date();

        //根据spuId获取需要删除的sku
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);

        skus.forEach(sku -> {
            //删除stock表
            this.stockMapper.deleteByPrimaryKey(sku.getId());

            //删除sku表
            this.skuMapper.delete(sku);
        });

        //新增sku及stock
        this.saveSkuAndStock(spuBo);

        //更新spu及spuDetail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(date);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        //发送消息
        sendMsg("update", spuBo.getId());
    }

    /**
     * 使用消息中间件来发送消息
     *
     * @param id   消息内容
     * @param type 通配符
     */
    private void sendMsg(String type, Long id) {
        try {
            //使用消息中间件来通知搜索及页面静态化，这里注意要使用try-catch来包裹。使它在运行时不要影响到主业务
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增sku及Stock方法
     *
     * @param spuBo
     */
    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            //新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(spuBo.getCreateTime());
            sku.setLastUpdateTime(spuBo.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据id查询spu
     *
     * @param id
     * @return
     */
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据skuId查询sku
     * @param skuId
     * @return
     */
    public Sku querySkuById(Long skuId) {
        return this.skuMapper.selectByPrimaryKey(skuId);
    }
}
