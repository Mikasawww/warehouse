package mmc.warehousemanagementsystem.controller;

import mmc.warehousemanagementsystem.entity.*;
import mmc.warehousemanagementsystem.mapper.AdminsMapper;
import mmc.warehousemanagementsystem.mapper.MaterialMapper;
import mmc.warehousemanagementsystem.mapper.OrderMapper;
import mmc.warehousemanagementsystem.mapper.WarehouseMapper;
import mmc.warehousemanagementsystem.utils.commonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Dantence
 * @date 2022/9/7
 */
@Controller
public class MaterialController {
    public final static String UPLOAD_PATH_PREFIX = "src/main/resources/static";
    @Value("${pro.uploadPath}")
    private String uploadPath;
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private AdminsMapper adminsMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    @PostMapping("/stock")
    public String stock(@RequestParam(name = "name") String name,
                        @RequestParam(name = "quantity") Integer quantity,
                        @RequestParam(name = "warehouse") Long warehouseId,
                        @RequestParam(name = "price") Integer price,
                        @RequestParam(name = "img", required = false) MultipartFile file,
                        HttpServletRequest request,
                        RedirectAttributesModelMap model) throws IOException {
        model.addFlashAttribute("section", "warehouseManagement");
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        if (StringUtils.isEmpty(name)) {
            model.addFlashAttribute("error", "货物名称不能为空!");
            return "redirect:/index";
        }
        if (quantity == null) {
            model.addFlashAttribute("error", "货物数量不能为空!");
            return "redirect:/index";
        }
        if (price == null) {
            model.addFlashAttribute("error", "物品单价不能为空!");
            return "redirect:/index";
        }
        if (warehouseId == null) {
            model.addFlashAttribute("error", "仓库编号不能为空!");
            return "redirect:/index";
        }
        if (quantity <= 0) {
            model.addFlashAttribute("error", "进货数量必须大于0!");
            return "redirect:/index";
        }
        if (price <= 0) {
            model.addFlashAttribute("error", "物品单价必须大于0!");
            return "redirect:/index";
        }
        Warehouse warehouse = warehouseMapper.selectByPrimaryKey(warehouseId);
        if (warehouse == null) {
            model.addFlashAttribute("error", "该仓库已经被删除!");
            return "redirect:/index";
        }
        if (warehouse.getOccupy() + quantity > warehouse.getCapacity()) {
            model.addFlashAttribute("error", "进货数量过多，仓库无法容纳!");
            return "redirect:/index";
        }
        if (admin.getManageWarehouse() == null || !admin.getManageWarehouse().equals(warehouseId)) {
            model.addFlashAttribute("error", "您不是该仓库的管理员!");
            return "redirect:/index";
        }
        name = name.trim();
        MaterialExample materialExample = new MaterialExample();
        materialExample.createCriteria().andNameEqualTo(name);
        List<Material> res = materialMapper.selectByExample(materialExample);
        //所有的仓库里都没有此货物
        if (res.size() <= 0) {
            Material material = new Material();
            material.setName(name);
            material.setQuantity(quantity);
            material.setPrice(price);
            material.setWarehouse(warehouseId);
            Long time = System.currentTimeMillis();
            material.setGmtCreated(time);
            material.setGmtModified(time);
            String url = handleImg(file);
            material.setImg(url);
            int rows = materialMapper.insert(material);
            if (rows != 1) {
                model.addFlashAttribute("error", "系统异常，请重试!");
                return "redirect:/index";
            }
            Warehouse warehouse1 = new Warehouse();
            warehouse1.setId(warehouseId);
            warehouse1.setOccupy(warehouse.getOccupy() + quantity);
            warehouse1.setGmtModified(System.currentTimeMillis());
            warehouseMapper.updateByPrimaryKeySelective(warehouse1);
        } else if (res.size() == 1) {
            if (!res.get(0).getWarehouse().equals(warehouseId)) {
                model.addFlashAttribute("error", "该货物在其他仓库中已存在!");
                return "redirect:/index";
            } else {
                Material material = new Material();
                material.setId(res.get(0).getId());
                material.setQuantity(quantity + res.get(0).getQuantity());
                material.setWarehouse(warehouseId);
                material.setGmtModified(System.currentTimeMillis());
                if (!file.isEmpty()) {
                    String url = handleImg(file);
                    material.setImg(url);
                }
                materialMapper.updateByPrimaryKeySelective(material);
                Warehouse warehouse1 = new Warehouse();
                warehouse1.setId(warehouseId);
                warehouse1.setOccupy(warehouse.getOccupy() + quantity);
                warehouse1.setGmtModified(System.currentTimeMillis());
                warehouseMapper.updateByPrimaryKeySelective(warehouse1);
            }
        }
        Order order = new Order();
        order.setQuantity(quantity);
        order.setType(1);
        if (res.size() > 0) {
            order.setMaterialName(res.get(0).getName());
            order.setSum(quantity * res.get(0).getPrice());
        } else {
            materialExample = new MaterialExample();
            materialExample.createCriteria().andNameEqualTo(name);
            order.setMaterialName(materialMapper.selectByExample(materialExample).get(0).getName());
            order.setSum(materialMapper.selectByExample(materialExample).get(0).getPrice() * quantity);
        }
        order.setAdminId(admin.getId());
        order.setGmtCreated(System.currentTimeMillis());
        orderMapper.insert(order);
        return "redirect:/index";
    }

    public String getUploadPath(HttpServletRequest request, String fileName) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + uploadPath + "/" + fileName;
    }

    public String handleImg(MultipartFile file) throws IOException {
        String newFileName = null;
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            newFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileName.substring(fileName.lastIndexOf("."));
            File readPath = new File(UPLOAD_PATH_PREFIX + uploadPath);
            if (!readPath.isDirectory()) {
                // 创建文件夹
                readPath.mkdirs();
            }
            File f = new File(readPath.getAbsolutePath() + File.separator + newFileName);
            file.transferTo(f);
        }
        return newFileName;
    }

    @Transactional
    @PostMapping("/ship")
    public String ship(@RequestParam(name = "name") String name,
                       @RequestParam(name = "quantity") Integer quantity,
                       @RequestParam(name = "warehouse") Long warehouseId,
                       HttpServletRequest request,
                       RedirectAttributesModelMap model) {
        model.addFlashAttribute("section", "warehouseManagement");
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        if (StringUtils.isEmpty(name)) {
            model.addFlashAttribute("error", "货物名称不能为空!");
            return "redirect:/index";
        }
        if (quantity == null) {
            model.addFlashAttribute("error", "货物数量不能为空!");
            return "redirect:/index";
        }
        if (warehouseId == null) {
            model.addFlashAttribute("error", "仓库编号不能为空!");
            return "redirect:/index";
        }
        if (quantity <= 0) {
            model.addFlashAttribute("error", "出货数量必须大于0!");
            return "redirect:/index";
        }
        Warehouse warehouse = warehouseMapper.selectByPrimaryKey(warehouseId);
        if (warehouse == null) {
            model.addFlashAttribute("error", "该仓库已经被删除!");
            return "redirect:/index";
        }
        if (warehouse.getOccupy() - quantity < 0) {
            model.addFlashAttribute("error", "出货数量过多!");
            return "redirect:/index";
        }
        if (admin.getManageWarehouse() == null || !admin.getManageWarehouse().equals(warehouseId)) {
            model.addFlashAttribute("error", "您不是该仓库的管理员!");
            return "redirect:/index";
        }
        name = name.trim();
        MaterialExample materialExample = new MaterialExample();
        materialExample.createCriteria().andNameEqualTo(name);
        List<Material> res = materialMapper.selectByExample(materialExample);
        if (res.size() <= 0 || !res.get(0).getWarehouse().equals(warehouseId)) {
            model.addFlashAttribute("error", "该仓库中没有此货物!");
            return "redirect:/index";
        } else if (res.get(0).getQuantity() - quantity > 0) {
            Warehouse warehouse1 = new Warehouse();
            warehouse1.setId(warehouseId);
            warehouse1.setOccupy(warehouse.getOccupy() - quantity);
            warehouse1.setGmtModified(System.currentTimeMillis());
            warehouseMapper.updateByPrimaryKeySelective(warehouse1);
            Material material = new Material();
            material.setId(res.get(0).getId());
            material.setQuantity(res.get(0).getQuantity() - quantity);
            material.setGmtModified(System.currentTimeMillis());
            materialMapper.updateByPrimaryKeySelective(material);
        } else {
            Warehouse warehouse1 = new Warehouse();
            warehouse1.setId(warehouseId);
            warehouse1.setOccupy(warehouse.getOccupy() - quantity);
            warehouse1.setGmtModified(System.currentTimeMillis());
            warehouseMapper.updateByPrimaryKeySelective(warehouse1);
            materialMapper.deleteByPrimaryKey(res.get(0).getId());
        }
        Order order = new Order();
        order.setQuantity(quantity);
        order.setType(0);
        order.setMaterialName(res.get(0).getName());
        order.setAdminId(admin.getId());
        order.setGmtCreated(System.currentTimeMillis());
        order.setSum(quantity * res.get(0).getPrice());
        orderMapper.insert(order);
        return "redirect:/index";
    }


    @GetMapping("/materialManage")
    public String materialManage(Model model,
                                 HttpServletRequest request) {
        Admins admin = commonUtils.getUserFromSession(request);
        if (admin == null) {
            model.addAttribute("error", "用户未登录!");
            return "redirect:/login";
        }
        MaterialExample example = new MaterialExample();
        example.setOrderByClause("gmt_modified desc");
        List<Material> materials = materialMapper.selectByExample(example);
        model.addAttribute("section", "materialManagement");
        model.addAttribute("allMaterials", materials);
        return "material";
    }
}
