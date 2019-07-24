package org.jeecg.modules.project.szph.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 项目收支平衡
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@Data
@TableName("zh_project_szph")
public class Szph implements Serializable {
    private static final long serialVersionUID = 1L;
    

	/**ID*/
	@TableId(type = IdType.UUID)
	private String id;
    /**描述月份*/
    @Excel(name = "月份", width = 15)
    private String ms;
	/**收入百分比*/
	@Excel(name = "收入金额", width = 15)
	private String spercent;
    /**盈利百分比*/
    @Excel(name = "盈利金额", width = 15)
    private String ypercent;
    /**支出百分比*/
    @Excel(name = "支出金额", width = 15)
    private String zpercent;
    /**项目ID*/
    private String projectId;
    /**创建人*/
    private String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

}
