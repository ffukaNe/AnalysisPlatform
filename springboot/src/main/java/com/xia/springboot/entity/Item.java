package com.xia.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("item")
@Data
public class Item {
    //  将数据库的字段和这个属性绑定,idType=AUTO设置主键自增
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String sellPoint;
    //    mybatis自动将数据库里面的下划线命名法转换成驼峰命名法
    //    private String nick_name;
    private BigDecimal taobaoPrice;
    private Integer stock;
    private String barcode;
    private String image;
    private Long cid;
    private Byte status;
    private Date created;
    private Date updated;
    private double qualityScore;
}
