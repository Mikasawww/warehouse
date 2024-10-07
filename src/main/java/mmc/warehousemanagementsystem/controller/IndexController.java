package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.dto.WarehouseDTO;
import mmc.warehousemanagementsystem.entity.Admins;
import mmc.warehousemanagementsystem.entity.Warehouse;
import mmc.warehousemanagementsystem.entity.WarehouseExample;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.mapper.WarehouseMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Controller
public class IndexController {
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private AdminsMapper adminsMapper;

    /**
     * 仓储管理系统首页，展示所有仓库的基本信息
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request){
        Admins admin = commonUtils.getUserFromSession(request);
        if(admin == null){
            model.addAttribute("error", "用户未登录!");
            return "login";
        }
        List<Warehouse> warehouses = warehouseMapper.selectByExample(new WarehouseExample());
        List<WarehouseDTO> warehouseDTOS = new ArrayList<>();
        for(Warehouse warehouse : warehouses){
            WarehouseDTO warehouseDTO = new WarehouseDTO();
            warehouseDTO.setId(warehouse.getId());
            warehouseDTO.setCapacity(warehouse.getCapacity());
            warehouseDTO.setOccupy(warehouse.getOccupy());
            warehouseDTO.setStatus(warehouse.getStatus());
            warehouseDTO.setRate((double) warehouse.getOccupy() / (double) warehouse.getCapacity());
            Admins user = adminsMapper.selectByPrimaryKey(warehouse.getCreator());
            warehouseDTO.setCreatorName(user.getName());
            warehouseDTOS.add(warehouseDTO);
        }
        model.addAttribute("warehouses", warehouseDTOS);
        model.addAttribute("section", "warehouseManagement");
        return "index";
    }
}
