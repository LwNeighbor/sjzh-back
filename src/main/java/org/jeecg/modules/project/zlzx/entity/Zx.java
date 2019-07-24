package org.jeecg.modules.project.zlzx.entity;

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
 * @Description: 总览折线
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@Data
@TableName("zh_zl_zx")
public class Zx implements Serializable {
    private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.UUID)
	private String id;
	/**描述*/
	@Excel(name = "描述", width = 15)
	private String xray;
	/**金额*/
	@Excel(name = "金额", width = 15)
	private String yray;
	/**年份*/
	@Excel(name = "年份", width = 15)
	private String years;
	/**排序*/
	@Excel(name = "排序", width = 15)
	private String sort;
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
