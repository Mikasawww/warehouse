package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.dto.ResultDTO;
import mmc.warehousemanagementsystem.entity.*;
import mmc.warehousemanagementsystem.enums.ErrorCodeEnum;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.mapper.MaterialMapper;
import mmc.warehousemanagementsystem.mapper.WarehouseMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Controller
public class WarehouseController {
    @Autowired
    private AdminsMapper adminsMapper;

    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 根据仓库编号删除对应的仓库，删除时保证仓库不为空
     *
     * @param warehouseId
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/removeWarehouse")
    public Object remove(@RequestParam(name = "id") Long warehouseId,
                         HttpServletRequest request) {
        Admins admin = commonUtils.getUserFromSession(request);
        ResultDTO<Void> resultDTO = new ResultDTO<>();
        if (admin == null) {
            resultDTO.setCode(ErrorCodeEnum.USER_NOT_LOGIN.getCode());
            resultDTO.setMsg(ErrorCodeEnum.USER_NOT_LOGIN.getMsg());
            return resultDTO;
        }
        Admins findedAdmin = adminsMapper.selectByPrimaryKey(admin.getId());
        if (findedAdmin == null) {
            resultDTO.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            resultDTO.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getMsg());
            return resultDTO;
        }
        if (!warehouseId.equals(findedAdmin.getManageWarehouse())) {
            resultDTO.setCode(ErrorCodeEnum.REMOVE_WAREHOUSE_PERMISSION_DENIED.getCode());
            resultDTO.setMsg(ErrorCodeEnum.REMOVE_WAREHOUSE_PERMISSION_DENIED.getMsg());
            return resultDTO;
        }
        Warehouse warehouse = warehouseMapper.selectByPrimaryKey(warehouseId);
        if (warehouse == null) {
            resultDTO.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            resultDTO.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getMsg());
            return resultDTO;
        }
        if (warehouse.getOccupy() > 0) {
            resultDTO.setCode(ErrorCodeEnum.REMOVE_WAREHOUSE_WAREHOUSE_NOT_EMPTY.getCode());
            resultDTO.setMsg(ErrorCodeEnum.REMOVE_WAREHOUSE_WAREHOUSE_NOT_EMPTY.getMsg());
            return resultDTO;
        }
        int rows = warehouseMapper.deleteByPrimaryKey(warehouseId);
        if (rows != 1) {
            resultDTO.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            resultDTO.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getMsg());
            return resultDTO;
        }
        resultDTO.setCode(ErrorCodeEnum.SUCESS.getCode());
        resultDTO.setMsg(ErrorCodeEnum.SUCESS.getMsg());
        return resultDTO;
    }


    @PostMapping("/createWarehouse")
    public String create(@RequestParam(name = "username") String username,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "capacity") Integer capacity,
                         RedirectAttributesModelMap model,
                         HttpServletRequest request) {
        model.addFlashAttribute("section", "warehouseManagement");
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addFlashAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        if (StringUtils.isEmpty(username)) {
            model.addFlashAttribute("error", "用户名不能为空!");
            return "redirect:/index";
        }
        if (StringUtils.isEmpty(password)) {
            model.addFlashAttribute("error", "密码不能为空!");
            return "redirect:/index";
        }
        if (capacity == null) {
            model.addFlashAttribute("error", "仓库容量不能为空!");
            return "redirect:/index";
        }
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        AdminsExample example = new AdminsExample();
        example.createCriteria().andNameEqualTo(username).andPasswordEqualTo(hashPassword);
        List<Admins> res = adminsMapper.selectByExample(example);
        if (res.size() <= 0) {
            model.addFlashAttribute("error", "用户名或密码错误!");
            return "redirect:/index";
        }
        if (capacity <= 0) {
            model.addFlashAttribute("error", "仓库容量必须大于0!");
            return "redirect:/index";
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setCreator(admin.getId());
        warehouse.setOccupy(0);
        warehouse.setCapacity(capacity);
        warehouse.setStatus(1);
        Long time = System.currentTimeMillis();
        warehouse.setGmtCreated(time);
        warehouse.setGmtModified(time);
        int rows = warehouseMapper.insert(warehouse);
        if (rows != 1) {
            model.addFlashAttribute("error", "密码不能为空!");
            return "redirect:/index";
        }
        return "redirect:/index";
    }

    @GetMapping("/warehouse/{id}")
    public String detail(@PathVariable(name = "id")Long id,
                         HttpServletRequest request,
                         Model model){
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        AdminsExample example = new AdminsExample();
        example.createCriteria().andManageWarehouseEqualTo(id);
        List<Admins> admins = adminsMapper.selectByExample(example);

        MaterialExample materialExample = new MaterialExample();
        materialExample.createCriteria().andWarehouseEqualTo(id);
        List<Material> materials = materialMapper.selectByExample(materialExample);
        model.addAttribute("admins", admins);
        model.addAttribute("materials", materials);
        model.addAttribute("warehouseId", id);
        model.addAttribute("section", "warehouseManagement");
        return "warehouse";
    }
}
