package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.CustomGenericException;
import com.hzq.common.ResponseCode;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Group;
import com.hzq.domain.User;
import com.hzq.domain.UserInfo;
import com.hzq.service.GroupService;
import com.hzq.service.UserInfoService;
import com.hzq.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * @Auther: blue
 * @Date: 2019/10/10
 * @Description: 专门处理文件
 * @version: 1.0
 */
@Controller
@RequestMapping("/file")
public class FileHandlerController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private GroupService groupService;

    /**
     * 处理上传头像
     * @param session 一次会话
     * @param avatar 头像图片
     * @return 返回通用对象
     */
    @RequestMapping( value = "/updateAvatar", method = RequestMethod.POST)
    public ServerResponse<String> updateAvatar(@RequestPart("avatar")MultipartFile avatar, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String fileName = System.currentTimeMillis()+avatar.getOriginalFilename();
        try {
            File dir = new File(Const.File_PATH+"//"+user.getId(),fileName);
            FileUtil.ByteToPhoto(avatar.getBytes(),dir);
        } catch (Exception e) {
            throw new CustomGenericException(ResponseCode.UPLOAD_FILE_ERROR.getCode(),ResponseCode.UPLOAD_FILE_ERROR.getDesc());
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setAvatar(fileName);
        ServerResponse<String> response = userInfoService.update(userInfo);
        response.setData(fileName);
        return response;
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
            File dir = new File(Const.File_PATH+"//"+id,fileName);
            FileUtil.ByteToPhoto(icon.getBytes(),dir);
        } catch (Exception e) {
            throw new CustomGenericException(ResponseCode.UPLOAD_FILE_ERROR.getCode(),ResponseCode.UPLOAD_FILE_ERROR.getDesc());
        }
        Group group = new Group();
        group.setGroupIcon(fileName);
        group.setId(id);
        return groupService.update(group);
    }



    /**
     * 专门处理发送消息中的文件处理
     * @param file 上传的文件
     * @param userId 用户id
     */
    @RequestMapping("/userId")
    //处理发送消息过程中发送的图片和文件，约定安卓自己随机生成名字
    public void handlerFile(@RequestPart("file") MultipartFile file, @PathVariable Integer userId) {
        try {
            File dir = new File(Const.File_PATH+"//"+userId,file.getOriginalFilename());
            FileUtil.ByteToPhoto(file.getBytes(),dir);
        } catch (Exception e) {
            throw new CustomGenericException(ResponseCode.UPLOAD_FILE_ERROR.getCode(),ResponseCode.UPLOAD_FILE_ERROR.getDesc());
        }
    }


}
