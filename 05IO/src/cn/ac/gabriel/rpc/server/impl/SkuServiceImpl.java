package cn.ac.gabriel.rpc.server.impl;

import cn.ac.gabriel.rpc.common.SkuService;

public class SkuServiceImpl implements SkuService {
    @Override
    public String findByName(String name) {
        return "sku: " + name;
    }
}
