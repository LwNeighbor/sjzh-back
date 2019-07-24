package org.jeecg.modules.project.cw.controller;

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
import org.jeecg.modules.project.cw.entity.Cw;
import org.jeecg.modules.project.cw.service.ICwService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.project.szph.entity.Szph;
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
 * @Description: 项目财务
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@RestController
@RequestMapping("/cw/cw")
@Slf4j
public class CwController {
	@Autowired
	private ICwService cwService;
	
	/**
	  * 分页列表查询
	 * @param cw
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Cw>> queryPageList(Cw cw,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Cw>> result = new Result<IPage<Cw>>();
		QueryWrapper<Cw> queryWrapper = QueryGenerator.initQueryWrapper(cw, req.getParameterMap());
		Page<Cw> page = new Page<Cw>(pageNo, pageSize);
		IPage<Cw> pageList = cwService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param cw
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Cw> add(@RequestBody Cw cw) {
		Result<Cw> result = new Result<Cw>();
		try {
			cwService.save(cw);
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
	 * @param cw
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Cw> edit(@RequestBody Cw cw) {
		Result<Cw> result = new Result<Cw>();
		Cw cwEntity = cwService.getById(cw.getId());
		if(cwEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = cwService.updateById(cw);
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
	public Result<Cw> delete(@RequestParam(name="id",required=true) String id) {
		Result<Cw> result = new Result<Cw>();
		Cw cw = cwService.getById(id);
		if(cw==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = cwService.removeById(id);
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
	public Result<Cw> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Cw> result = new Result<Cw>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.cwService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Cw> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Cw> result = new Result<Cw>();
		Cw cw = cwService.getById(id);
		if(cw==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(cw);
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
      QueryWrapper<Cw> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Cw cw = JSON.parseObject(deString, Cw.class);
              queryWrapper = QueryGenerator.initQueryWrapper(cw, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Cw> pageList = cwService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "项目财务列表");
      mv.addObject(NormalExcelConstants.CLASS, Cw.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("项目财务列表数据", "", "导出信息"));
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
  	  String projectId = request.getParameter("projectId");
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
          	  cwService.remove(new QueryWrapper<Cw>().eq("project_id",projectId));
              List<Cw> listCws = ExcelImportUtil.importExcel(file.getInputStream(), Cw.class, params);
              for (Cw cwExcel : listCws) {
              	  cwExcel.setProjectId(projectId);
                  cwService.save(cwExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCws.size());
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
