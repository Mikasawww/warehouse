package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.entity.Admins;
import mmc.warehousemanagementsystem.entity.AdminsExample;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Controller
public class RegisterController {
    @Autowired
    private AdminsMapper adminsMapper;

    @GetMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    /**
     * 注册用户，用户名、手机号、密码和重复密码非空，且密码和重复密码一致
     * @param username
     * @param phone
     * @param password
     * @param repeatPassword
     * @param model
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestParam(name = "username")String username,
                           @RequestParam(name = "phone")String phone,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "repeatPassword") String repeatPassword,
                           Model model){

        model.addAttribute("username", username);
        model.addAttribute("phone", phone);
        model.addAttribute("password", password);
        model.addAttribute("repeatPassword", repeatPassword);

        if(StringUtils.isEmpty(username)){
            model.addAttribute("error", "用户名不能为空!");
            return "register";
        }
        if(StringUtils.isEmpty(phone)){
            model.addAttribute("error", "手机号不能为空!");
            return "register";
        }
        if(StringUtils.isEmpty(password)){
            model.addAttribute("error", "密码不能为空!");
            return "register";
        }
        if(StringUtils.isEmpty(repeatPassword)){
            model.addAttribute("error", "重复密码不能为空!");
            return "register";
        }
        if(!password.equals(repeatPassword)){
            model.addAttribute("error", "两次密码输入不一致!");
            return "register";
        }

        AdminsExample example = new AdminsExample();
        example.createCriteria().andNameEqualTo(username);
        List<Admins> res = adminsMapper.selectByExample(example);
        if(res.size() > 0){
            model.addAttribute("error", "该用户名已存在!");
            return "register";
        }
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        Admins admin = new Admins();
        admin.setName(username);
        admin.setPhone(phone);
        admin.setPassword(hashPassword);
        Long time = System.currentTimeMillis();
        admin.setGmtCreated(time);
        admin.setGmtModified(time);
        int rows = adminsMapper.insert(admin);
        if(rows != 1){
            model.addAttribute("error", "系统异常，请重试!");
            return "register";
        }
        return "login";
    }
}
