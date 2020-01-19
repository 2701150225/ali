package com.ali.item.service;


import com.ali.item.mapper.SpecGroupMapper;
import com.ali.item.mapper.SpecParamMapper;
import com.ali.item.pojo.SpecGroup;
import com.ali.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询参数组
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    public List<SpecParam> queryParam(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setGeneric(generic);
        param.setSearching(searching);
        return this.specParamMapper.select(param);
    }

    /**
     * 根据分类id查询规格组，及组下的所有规模参数
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupWithParam(Long cid) {
        List<SpecGroup> groups = this.queryGroupsByCid(cid);

        groups.forEach(group -> {
            group.setParams(this.queryParam(group.getId(), null, null, null));
        });

        return groups;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSpecGroup(SpecGroup specGroup) {
        //insert 和 insertSelective的区别
        //insert 那么所有的字段都会添加一遍即使没有值
        //inserSelective就会只给有值的字段赋值（会对传进来的值做非空判断）

        specGroupMapper.insert(specGroup);

    }

    @Transactional(rollbackFor = Exception.class)
    public void editSpecGroup(SpecGroup specGroup) {
        specGroupMapper.editSpecGroup(specGroup);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSpecGroup(Long id) {
        specGroupMapper.deleteSpecGroup(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSpecParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editSpecParam(SpecParam specParam) {
        specParamMapper.editSpecParam(specParam);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSpecParam(Long id) {
        specParamMapper.deleteSpecParam(id);
    }
}
