package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.enums.FileTypeEnum;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Group;
import com.hzq.domain.User;
import com.hzq.domain.UserInfo;
import com.hzq.service.GroupService;
import com.hzq.service.UserInfoService;
import com.hzq.utils.FileUtil;
import com.hzq.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @Auther: blue
 * @Date: 2019/10/10
 * @Description: 专门处理文件
 * @version: 1.0
 */
@RestController
@RequestMapping("/file")
public class FileHandlerController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private GroupService groupService;
    //日志打印
    private static Logger LOGGER = LoggerFactory.getLogger(FileHandlerController.class);

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
            //输入文件类型
            FileTypeEnum fileTypeEnum = Objects.requireNonNull(FileUtil.getFileType(avatar.getInputStream()));
            LOGGER.debug(fileTypeEnum.getExt());
            if (!FileUtil.isPhoto(fileTypeEnum)) {
                throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"上传文件失败");
            }
            File f = new File(Const.FILE_PATH +"//"+user.getId());
            if (!f.exists()) {
                FileUtil.CheckPath(f);
            }
            File dir = new File(Const.FILE_PATH +"//"+user.getId(),fileName);
            FileUtil.ByteToPhoto(avatar.getBytes(),dir);
            UserInfo userInfo = userInfoService.queryUserByName(user.getUsername()).getData();
            String avatarName = userInfo.getAvatar();
            if (!avatarName.equals(Const.DEFAULT_AVATAR)) {
                File deleteFile = new File(StringUtil.getPhotoUrl(avatarName));
                LOGGER.debug("剪切过后的路径: "+StringUtil.getPhotoUrl(avatarName));
                if (!deleteFile.delete()) {
                    LOGGER.debug("存储用户头像的时候，删除之前头像出现了错误");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"上传文件失败");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setAvatar(Const.IMAGE_PATH+user.getId()+"//"+fileName);
        ServerResponse<String> response = userInfoService.update(userInfo);
        response.setData(Const.IMAGE_PATH+user.getId()+"//"+fileName);
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
        //输入文件类型
        try {
            FileTypeEnum fileTypeEnum = Objects.requireNonNull(FileUtil.getFileType(icon.getInputStream()));
            LOGGER.debug(fileTypeEnum.getExt());
            if (!FileUtil.isPhoto(fileTypeEnum)) {
                throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"不是图片类型");
            }
            File f = new File(Const.FILE_PATH +"//"+id);
            if (!f.exists()) {
                FileUtil.CheckPath(f);
            }
            File dir = new File(Const.FILE_PATH +"//"+id,fileName);
            FileUtil.ByteToPhoto(icon.getBytes(),dir);
            Group group = groupService.select(id).getData();
            String groupIcon = group.getGroupIcon();
            if (!groupIcon.equals(Const.DEFAULT_AVATAR)) {
                File deleteFile = new File(StringUtil.getPhotoUrl(groupIcon));
                if (!deleteFile.delete()) {
                    LOGGER.debug("存储群头像的时候，删除之前头像出现了错误");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"上传文件失败");
        }
        Group group = new Group();
        group.setGroupIcon(Const.IMAGE_PATH+id+"//"+fileName);
        group.setId(id);
        ServerResponse<String> response = groupService.update(group);
        response.setData((Const.IMAGE_PATH+id+"//"+fileName));
        return response;
    }

    /**
     * 专门处理发送消息中的文件处理
     * @param file 上传的文件
     * @param userId 用户id
     */
    @RequestMapping("/{userId}")
    public ServerResponse<String> handlerFile(@RequestPart("file") MultipartFile file, @PathVariable Integer userId) {
        String fileName = System.currentTimeMillis()+file.getOriginalFilename();
        try {
            File f = new File(Const.FILE_PATH +"//"+userId);
            if (!f.exists()) {
                FileUtil.CheckPath(f);
            }
            File dir = new File(Const.FILE_PATH +"//"+userId,fileName);
            FileUtil.ByteToPhoto(file.getBytes(),dir);
        } catch (IOException e) {
            e.printStackTrace();
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"上传文件失败");
        }
        return ServerResponse.createBySuccess(Const.IMAGE_PATH+userId+"//"+fileName);
    }

    /**
     * 根据路径删除文件
     * @param path 文件路径
     */
    @RequestMapping("/deleteFile")
    public void deleteFile(@RequestBody String path) {
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                LOGGER.debug("文件不存在");
                throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"上传文件失败");
            }
            if (!dir.delete()) {
                LOGGER.debug("删除文件出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"上传文件失败");
        }
    }

}
