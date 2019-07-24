package org.jeecg.modules.project.detail.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.detail.entity.Detail;
import org.jeecg.modules.project.detail.service.IDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Title: Controller
 * @Description: 使用说明
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/detail/detail")
@Slf4j
public class DetailController {
	@Autowired
	private IDetailService detailService;
	
	/**
	  * 分页列表查询
	 * @param detail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Detail>> queryPageList(Detail detail,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Detail>> result = new Result<IPage<Detail>>();
		QueryWrapper<Detail> queryWrapper = QueryGenerator.initQueryWrapper(detail, req.getParameterMap());
		Page<Detail> page = new Page<Detail>(pageNo, pageSize);
		IPage<Detail> pageList = detailService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param detail
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Detail> add(@RequestBody Detail detail) {
		Result<Detail> result = new Result<Detail>();
		try {
			detailService.save(detail);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param detail
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Detail> edit(@RequestBody Detail detail) {
		Result<Detail> result = new Result<Detail>();
		Detail detailEntity = detailService.getById(detail.getId());
		if(detailEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = detailService.updateById(detail);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<Detail> delete(@RequestParam(name="id",required=true) String id) {
		Result<Detail> result = new Result<Detail>();
		Detail detail = detailService.getById(id);
		if(detail==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = detailService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<Detail> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Detail> result = new Result<Detail>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.detailService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<Detail> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Detail> result = new Result<Detail>();
		Detail detail = detailService.getById(id);
		if(detail==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(detail);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<Detail> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Detail detail = JSON.parseObject(deString, Detail.class);
              queryWrapper = QueryGenerator.initQueryWrapper(detail, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Detail> pageList = detailService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "使用说明列表");
      mv.addObject(NormalExcelConstants.CLASS, Detail.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("使用说明列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<Detail> listDetails = ExcelImportUtil.importExcel(file.getInputStream(), Detail.class, params);
              for (Detail detailExcel : listDetails) {
                  detailService.save(detailExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listDetails.size());
          } catch (Exception e) {
              log.error(e.getMessage());
              return Result.error("文件导入失败！");
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
