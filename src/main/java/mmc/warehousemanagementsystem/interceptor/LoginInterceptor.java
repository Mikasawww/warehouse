package mmc.warehousemanagementsystem.interceptor;

import mmc.warehousemanagementsystem.entity.Admins;
import mmc.warehousemanagementsystem.entity.AdminsExample;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private AdminsMapper adminsMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = commonUtils.getToken(request);
        if(token != null){
            AdminsExample example = new AdminsExample();
            example.createCriteria().andTokenEqualTo(token);
            List<Admins> userList = adminsMapper.selectByExample(example);
            if(userList.size() > 0){
                request.getSession().setAttribute("user", userList.get(0));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
