package mmc.warehousemanagementsystem.entity;

public class Order {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.id
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.material_name
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private String materialName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.quantity
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private Integer quantity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.admin_id
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private Long adminId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.sum
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private Integer sum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.type
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column orders.gmt_created
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    private Long gmtCreated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.id
     *
     * @return the value of orders.id
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.id
     *
     * @param id the value for orders.id
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.material_name
     *
     * @return the value of orders.material_name
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.material_name
     *
     * @param materialName the value for orders.material_name
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.quantity
     *
     * @return the value of orders.quantity
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.quantity
     *
     * @param quantity the value for orders.quantity
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.admin_id
     *
     * @return the value of orders.admin_id
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.admin_id
     *
     * @param adminId the value for orders.admin_id
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.sum
     *
     * @return the value of orders.sum
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public Integer getSum() {
        return sum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.sum
     *
     * @param sum the value for orders.sum
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setSum(Integer sum) {
        this.sum = sum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.type
     *
     * @return the value of orders.type
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.type
     *
     * @param type the value for orders.type
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column orders.gmt_created
     *
     * @return the value of orders.gmt_created
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public Long getGmtCreated() {
        return gmtCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column orders.gmt_created
     *
     * @param gmtCreated the value for orders.gmt_created
     *
     * @mbg.generated Fri Sep 09 11:54:38 CST 2022
     */
    public void setGmtCreated(Long gmtCreated) {
        this.gmtCreated = gmtCreated;
    }
}