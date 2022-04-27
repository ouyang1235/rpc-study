package cn.ouyang.test.netty2.demo1.web;

import cn.ouyang.test.netty2.demo1.server.NettyServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nettyserver")
public class NettyController {

    @Resource
    private NettyServer nettyServer;


    @GetMapping("/localAddress")
    public String localAddress(){
        return "nettyServer localAddress " + nettyServer.getChannel().localAddress();
    }

    @GetMapping("/isOpen")
    public String isOpen(){
        return "nettyServer isOpen  " + nettyServer.getChannel().isOpen();
    }


}
