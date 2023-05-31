package cn.ac.gabriel.rpc.server.impl;

import cn.ac.gabriel.rpc.common.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String findById(int id) {
        return "user{ id=" + id + ", name='Gabriel' }";
    }
}
