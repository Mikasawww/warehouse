package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.entity.Admins;
import mmc.warehousemanagementsystem.entity.AdminsExample;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Controller
public class LoginController {
    @Autowired
    private AdminsMapper adminsMapper;

    /**
     * 免密登录，用户第一次登录生成token存入数据库，并将token存至cookie
     * 以后用户访问携带cookie，拦截器LoginInterceptor根据cookie从数据库中查出用户，
     * 如果存在用户，则向session中存入用户，否则不存
     * 这样可以通过session的检测实现免密登录
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request,
                        RedirectAttributesModelMap model){
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin != null){
            model.addFlashAttribute("section", "warehouseManagement");
            return "redirect:/index";
        }
        return "login";
    }

    /**
     * 根据用户名和密码登录
     * @param username
     * @param password
     * @param response
     * @param model
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam(name = "username")String username,
                        @RequestParam(name = "password")String password,
                        HttpServletResponse response,
                        Model model){
        model.addAttribute("username", username);
        model.addAttribute("password", password);

        if(StringUtils.isEmpty(username)){
            model.addAttribute("error", "用户名不能为空!");
            return "login";
        }
        if(StringUtils.isEmpty(password)){
            model.addAttribute("error", "密码不能为空!");
            return "login";
        }
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        AdminsExample example = new AdminsExample();
        example.createCriteria().andNameEqualTo(username).andPasswordEqualTo(hashPassword);
        List<Admins> res = adminsMapper.selectByExample(example);
        if(res.size() == 0){
            model.addAttribute("error", "用户名或密码错误!");
            return "login";
        }
        if(res.size() > 1){
            model.addAttribute("error", "系统异常，请重试!");
            return "login";
        }
        String token = UUID.randomUUID().toString();
        Admins admin = new Admins();
        admin.setToken(token);
        int rows = adminsMapper.updateByExampleSelective(admin, example);
        if(rows != 1){
            model.addAttribute("error", "系统异常，请重试!");
            return "login";
        }
        response.addCookie(new Cookie("token", token));
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model){
        Admins admin = commonUtils.getUserFromSession(request);
        if(admin == null){
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
