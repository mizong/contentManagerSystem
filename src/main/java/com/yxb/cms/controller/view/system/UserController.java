package com.yxb.cms.controller.view.system;


import com.yxb.cms.architect.constant.BussinessCode;
import com.yxb.cms.architect.utils.BussinessMsgUtil;
import com.yxb.cms.controller.view.BaseController;
import com.yxb.cms.dao.DbUserMapper;
import com.yxb.cms.domain.dto.BussinessMsg;
import com.yxb.cms.domain.vo.DbUser;
import com.yxb.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 系统管理-用户管理
 *
 * @author YXB
 * @date 2019/01/15
 */
@Controller
@RequestMapping(value = "/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DbUserMapper userMapper;



    /**
     * 跳转到用户信息页面
     *
     * @return
     */
    @RequestMapping("/user_page.action")
    public String tUserPagePage() {
        return "system/user_page";
    }

    /**
     * 用户信息分页展示
     *
     * @param user 用户信息对象
     * @return
     */
    @RequestMapping("/ajax_user_list.action")
    @ResponseBody
    public Map<String, Object> ajaxUserList(DbUser user) {

        return userService.selectUserListByPage(user);
    }

    /**
     * 跳转到用户新增页面
     *
     * @return
     */
    @RequestMapping("/user_add.action")
    public String userAddPage(Model model) {
        model.addAttribute("pageFlag", "addPage");
        return "system/user_edit";
    }
    /**
     * 跳转用户编辑页面
     *
     * @return
     */
    @RequestMapping("/user_update.action")
    public String userUpdatePage(Model model,String userId) {
        model.addAttribute("pageFlag", "updatePage");
        DbUser user = userMapper.selectByPrimaryKey(userId);
        model.addAttribute("user",user);

        return "system/user_edit";
    }


    /**
     * 保存用户信息
     * @param user 用户对象
     * @return
     */
    @RequestMapping(value="/ajax_save_user.action")
    @ResponseBody
    public BussinessMsg ajaxSaveUser(DbUser user){
        try {
            return userService.saveOrUpdateUser(user,this.getCurrentUserId());
        } catch (Exception e) {
            log.error("保存用户信息内部错误{}", e.getMessage(), e);
            return BussinessMsgUtil.returnCodeMessage(BussinessCode.USER_SAVE_ERROR);
        }

    }



    /**
     * 重置用户密码
     * @param userId  用户Id
     * @return
     */
    @RequestMapping("/ajax_reset_user_pwd.action")
    @ResponseBody
    public BussinessMsg resetUserPwd(String userId) {
        try {
            return userService.resetUserPwd(userId,this.getCurrentUserId());
        } catch (Exception e) {
            log.error("重置用户密码方法内部错误{}",e.getMessage(),e);
            return BussinessMsgUtil.returnCodeMessage(BussinessCode.RESET_USER_PWD_ERROR);

        }
    }


    /**
     * 删除用户信息
     * @param userId  用户Id
     * @return
     */
    @RequestMapping("/ajax_del_user_info.action")
    @ResponseBody
    public BussinessMsg delUserInfo(String userId) {
        try {
            return userService.delUserInfo(userId,this.getCurrentUserId());
        } catch (Exception e) {
            log.error("删除用户信息方法内部错误{}",e.getMessage(),e);
            return BussinessMsgUtil.returnCodeMessage(BussinessCode.DELETE_USER_ERROR);

        }
    }

    /**
     * 跳转到用户个人详情页面
     * @param model
     * @return
     */
    @RequestMapping("/user_detail_page.action")
    public String userDetailPage(Model model, String userId){
        DbUser user = userMapper.selectUserByUserId(userId);
        model.addAttribute("user",user);
        return "system/user_detail";
    }
}
