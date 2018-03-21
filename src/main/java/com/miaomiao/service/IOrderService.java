package com.miaomiao.service;

import com.miaomiao.common.ServerResponse;

public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);
}
