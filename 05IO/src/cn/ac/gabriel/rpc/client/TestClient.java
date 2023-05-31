package cn.ac.gabriel.rpc.client;

import cn.ac.gabriel.rpc.common.SkuService;
import cn.ac.gabriel.rpc.common.UserService;

public class TestClient {
    public static void main(String[] args) {
        SkuService skuService = (SkuService) RPCProxy.create(SkuService.class);
        System.out.println(skuService.findByName("Gabriel"));

        UserService userService = (UserService) RPCProxy.create(UserService.class);
        System.out.println(userService.findById(1));
    }
}
