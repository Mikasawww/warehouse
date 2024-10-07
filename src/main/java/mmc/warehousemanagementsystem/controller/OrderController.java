package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.dto.OrderDTO;
import mmc.warehousemanagementsystem.entity.Admins;
import mmc.warehousemanagementsystem.entity.Material;
import mmc.warehousemanagementsystem.entity.Order;
import mmc.warehousemanagementsystem.entity.OrderExample;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.mapper.MaterialMapper;
import mmc.warehousemanagementsystem.mapper.OrderMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dantence
 * @date 2022/9/8
 */
@Controller
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AdminsMapper adminsMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @GetMapping("/billManage")
    public String showBill(HttpServletRequest request,
                           Model model){
        model.addAttribute("section", "billManagement");
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        OrderExample example = new OrderExample();
        example.setOrderByClause("gmt_created desc");
        List<Order> bills = orderMapper.selectByExample(example);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for(Order bill : bills){
            OrderDTO orderDTO = new OrderDTO();
            Admins user = adminsMapper.selectByPrimaryKey(bill.getAdminId());
            orderDTO.setAdminName(user.getName());
            orderDTO.setMaterialName(bill.getMaterialName());
            orderDTO.setId(bill.getId());
            orderDTO.setQuantity(bill.getQuantity());
            orderDTO.setSum(bill.getSum());
            orderDTO.setGmtCreated(bill.getGmtCreated());
            if(bill.getType() == 1){
                orderDTO.setTypeName("进货");
            } else {
                orderDTO.setTypeName("出货");
            }
            orderDTOS.add(orderDTO);
        }
        model.addAttribute("bills", orderDTOS);
        return "bill";
    }
}
