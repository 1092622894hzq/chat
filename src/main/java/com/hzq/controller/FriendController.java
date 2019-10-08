package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Friend;
import com.hzq.domain.User;
import com.hzq.service.FriendService;
import com.hzq.utils.JwtUil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@RequestMapping("/friend")
@Controller
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 添加好友
     * @param friend 好友信息  用户id和好友id必须有
     * @return 返回通用对象
     */
    @RequestMapping( value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> insert(@RequestBody Friend friend) {
        return friendService.insert(friend);
    }

    /**
     * 更新好友信息
     * @param friend 好友信息   用户id和好友id必须有
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> update(@RequestBody Friend friend) {
        return friendService.update(friend);
    }

    /**
     * 根据好友id删除好友
     * @param friendId 好友id
     * @param session  一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{friendId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> delete(@PathVariable Integer friendId, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return friendService.delete(user.getId(),friendId);
    }

    /**
     * 根据用户id查询用户所有好友
     * @param session 一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Friend>> selectAll(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return friendService.selectAll(user.getId());
    }

    /**
     * 根据好友昵称查询好友
     * @param session 一次会话
     * @param friendName 好友昵称
     * @return 返回通用对象
     */
    @RequestMapping(value = "/selectFriend/{friendName}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Friend> selectFriendByFriendName(@PathVariable String friendName, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return friendService.selectFriendByFriendName(user.getId(),friendName);
    }

}
