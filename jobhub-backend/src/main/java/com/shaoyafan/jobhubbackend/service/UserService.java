package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.user.*;
import com.shaoyafan.jobhubbackend.model.vo.user.LoginUserVO;
import com.shaoyafan.jobhubbackend.model.vo.user.UserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
* @author SYF
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return 新用户 id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 更新个人信息
     *
     * @param request
     * @return
     */
    Boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest, HttpServletRequest request);

    /**
     * 上传头像
     *
     * @param file
     * @param request
     * @return
     */
    Boolean uploadAvatar(MultipartFile file, HttpServletRequest request);

    /**
     * 更新密码
     *
     * @param userUpdatePwdRequest
     * @param request
     * @return
     */
    Boolean updatePassword(UserUpdatePwdRequest userUpdatePwdRequest, HttpServletRequest request);

    /**
     * 审核用户
     *
     * @param id
     * @return
     */
    Boolean reviewUser(Long id);

    /**
     * 冻结用户
     *
     * @param id
     * @return
     */
    Boolean freezeUser(Long id);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user 用户
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息列表
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
