package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.entity.Admins;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dantence
 * @date 2022/9/8
 */
@Controller
public class AdminController {
    @Autowired
    private AdminsMapper adminsMapper;

    @GetMapping("/manage/{id}")
    public String manage(@PathVariable(name = "id")Long warehouseId,
                         HttpServletRequest request,
                         RedirectAttributesModelMap model){
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        if(admin.getManageWarehouse() != null && admin.getManageWarehouse().equals(warehouseId)){
            model.addFlashAttribute("error", "您已经是该仓库的管理员了!");
            return "redirect:/index";
        }
        Admins user = new Admins();
        user.setId(admin.getId());
        user.setManageWarehouse(warehouseId);
        int rows = adminsMapper.updateByPrimaryKeySelective(user);
        if(rows != 1){
            model.addFlashAttribute("error", "系统异常，请重试!");
            return "redirect:/index";
        }
        return "redirect:/index";
    }
}
