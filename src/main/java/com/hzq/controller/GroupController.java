package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.CustomGenericException;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Group;
import com.hzq.domain.User;
import com.hzq.service.GroupService;
import com.hzq.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 创建群聊
     * @param group 群信息
     * @param session 一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServerResponse<String> insert(@RequestBody Group group, HttpSession session) {
        return groupService.insert(group,session);
    }

    /**
     * 删除群聊
     * @param id 群聊id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ServerResponse<String> delete(@PathVariable Integer id) {
        return groupService.delete(id);
    }

    /**
     * 更新群聊信息
     * @param group 要更新的群聊消息，必须包含群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<String> update(@RequestBody Group group) {
        return groupService.update(group);
    }

    /**
     * 选出所有群聊
     * @param session 一次会话
     * @return 返回存有所有群聊的通用对象
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public ServerResponse<List<Group>> selectAll(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return groupService.selectAll(user.getId());
    }


    /**
     * 处理上传头像
     * @param id 群id
     * @param icon 头像图片
     * @return 返回通用对象
     */
    @RequestMapping( value = "/updateIcon/{id}", method = RequestMethod.POST)
    public ServerResponse<String> updateIcon(@RequestPart("icon") MultipartFile icon, @PathVariable Integer id){
        String fileName = System.currentTimeMillis()+icon.getOriginalFilename();
        try {
            File dir = new File(Const.AVATAR_PATH,fileName);
            FileUtil.ByteToPhoto(icon.getBytes(),dir);
        } catch (Exception e) {
            throw new CustomGenericException(40, "上传文件出错了");
        }
        Group group = new Group();
        group.setGroupIcon(fileName);
        group.setId(id);
        return groupService.update(group);
    }


}
