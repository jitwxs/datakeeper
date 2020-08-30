package com.github.jitwxs.datakeeper.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.jitwxs.datakeeper.bean.User;
import com.github.jitwxs.datakeeper.datakeeper.DataKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jitwxs
 * @date 2019年08月18日 18:42
 */
@RestController
public class DemoController {
    @Autowired
    private DataKeeper dataKeeper;

    @GetMapping("/user/white/{userId}")
    public JSONObject getWhiteUser(@PathVariable Long userId) {
        JSONObject result = new JSONObject();

        User user = dataKeeper.getWhiteUser(userId);
        if(user == null) {
            result.put("status", false);
            result.put("message", "user not white user");
        } else {
            result.put("status", true);
            result.put("data", user);
        }
        return result;
    }
}
