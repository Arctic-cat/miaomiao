package com.miaomiao.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miaomiao.common.ServerResponse;
import com.miaomiao.dao.CategoryMapper;
import com.miaomiao.pojo.Category;
import com.miaomiao.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = (Logger) LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName,Integer parentId) {
        if (parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类时参数错误");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);//这个分类是可用的

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }

        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类时参数错误");
        }

        Category category = new Category();
        category.setParentId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);

        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类成功");
        }

        return ServerResponse.createByErrorMessage("更新品类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点id和孩子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse selectCategoryAndDeepChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归算法,算出子节点
    private Set<Category> findChildCategory( Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找子节点, 设定退出条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
