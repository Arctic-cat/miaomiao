package com.miaomiao.service;

import com.miaomiao.common.ServerResponse;
import com.miaomiao.pojo.Product;
import com.miaomiao.vo.ProductDetailVo;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
}
