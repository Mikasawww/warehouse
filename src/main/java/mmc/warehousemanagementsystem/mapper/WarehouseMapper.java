package mmc.warehousemanagementsystem.mapper;

import java.util.List;
import mmc.warehousemanagementsystem.entity.Warehouse;
import mmc.warehousemanagementsystem.entity.WarehouseExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface WarehouseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    long countByExample(WarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int deleteByExample(WarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int insert(Warehouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int insertSelective(Warehouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    List<Warehouse> selectByExampleWithRowbounds(WarehouseExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    List<Warehouse> selectByExample(WarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    Warehouse selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int updateByExampleSelective(@Param("record") Warehouse record, @Param("example") WarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int updateByExample(@Param("record") Warehouse record, @Param("example") WarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int updateByPrimaryKeySelective(Warehouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table warehouse
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    int updateByPrimaryKey(Warehouse record);
}