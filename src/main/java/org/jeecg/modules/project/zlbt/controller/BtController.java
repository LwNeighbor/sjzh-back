package org.jeecg.modules.project.zlbt.controller;

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
import org.jeecg.modules.project.zlbt.entity.Bt;
import org.jeecg.modules.project.zlbt.service.IBtService;

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
 * @Description: 总览图表
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@RestController
@RequestMapping("/zlbt/bt")
@Slf4j
public class BtController {
	@Autowired
	private IBtService btService;
	
	/**
	  * 分页列表查询
	 * @param bt
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Bt>> queryPageList(Bt bt,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Bt>> result = new Result<IPage<Bt>>();
		QueryWrapper<Bt> queryWrapper = QueryGenerator.initQueryWrapper(bt, req.getParameterMap());
		Page<Bt> page = new Page<Bt>(pageNo, pageSize);
		IPage<Bt> pageList = btService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param bt
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Bt> add(@RequestBody Bt bt) {
		Result<Bt> result = new Result<Bt>();
		try {
			btService.save(bt);
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
	 * @param bt
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Bt> edit(@RequestBody Bt bt) {
		Result<Bt> result = new Result<Bt>();
		Bt btEntity = btService.getById(bt.getId());
		if(btEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = btService.updateById(bt);
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
	public Result<Bt> delete(@RequestParam(name="id",required=true) String id) {
		Result<Bt> result = new Result<Bt>();
		Bt bt = btService.getById(id);
		if(bt==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = btService.removeById(id);
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
	public Result<Bt> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Bt> result = new Result<Bt>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.btService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Bt> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Bt> result = new Result<Bt>();
		Bt bt = btService.getById(id);
		if(bt==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bt);
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
      QueryWrapper<Bt> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Bt bt = JSON.parseObject(deString, Bt.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bt, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Bt> pageList = btService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "总览图表列表");
      mv.addObject(NormalExcelConstants.CLASS, Bt.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("总览图表列表数据", "注意:1.完成为Y,未完成为N	2.请填写整数数字", "导出信息"));
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
              List<Bt> listBts = ExcelImportUtil.importExcel(file.getInputStream(), Bt.class, params);
              for (Bt btExcel : listBts) {
                  btService.save(btExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listBts.size());
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
