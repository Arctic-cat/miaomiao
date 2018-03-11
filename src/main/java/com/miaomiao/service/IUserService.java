package com.miaomiao.service;

import com.miaomiao.common.ServerResponse;
import com.miaomiao.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);
}
