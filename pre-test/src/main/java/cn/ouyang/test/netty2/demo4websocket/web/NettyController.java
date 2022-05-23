package cn.ouyang.test.netty2.demo4websocket.web;

import cn.ouyang.test.netty2.demo4websocket.server.NettyServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class NettyController {

    @Resource
    private NettyServer nettyServerWebSocket;

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("name","xiaohu");
        return "index";
    }


}
