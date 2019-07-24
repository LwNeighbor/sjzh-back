package org.jeecg.modules.project.zlbt.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 总览图表
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@Data
@TableName("zh_zl_bt")
public class Bt implements Serializable {
    private static final long serialVersionUID = 1L;
    

	/**ID*/
	@TableId(type = IdType.UUID)
	private String id;
	/**完成情况 Y/完成  N.未完成*/
	@Excel(name = "完成情况(Y/完成  N.未完成)", width = 15)
	@Dict(dicCode = "is_down")
	private String isDone;
	/**百分比*/
	@Excel(name = "百分比", width = 15)
	private String percent;
	/**年份*/
	@Excel(name = "年份", width = 15)
	private String years;
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
