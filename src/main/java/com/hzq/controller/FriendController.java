package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.domain.Friend;
import com.hzq.domain.User;
import com.hzq.service.FriendService;
import com.hzq.vo.FriendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 好友
 * @version: 1.0
 */
@RequestMapping("/friend")
@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 更新好友信息
     * @param friend 好友信息   用户id和好友id必须有
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<FriendVo> update(@RequestBody Friend friend) {
        friendService.update(friend);
        return friendService.selectFriendByFriendId(friend.getUserId(),friend.getFriendId());
    }

    /**
     * 根据好友id删除好友
     * @param friendId 好友id
     * @param session  一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{friendId}", method = RequestMethod.GET)
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
    public ServerResponse<List<FriendVo>> selectAll(HttpSession session) {
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
    public ServerResponse<FriendVo> selectFriendByFriendName(@PathVariable String friendName, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return friendService.selectFriendByFriendName(user.getId(),friendName);
    }
}
