package org.jeecg.modules.project.szph.controller;

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
import org.jeecg.modules.project.szph.entity.Szph;
import org.jeecg.modules.project.szph.service.ISzphService;

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
 * @Description: 项目收支平衡
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@RestController
@RequestMapping("/szph/szph")
@Slf4j
public class SzphController {
	@Autowired
	private ISzphService szphService;
	
	/**
	  * 分页列表查询
	 * @param szph
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Szph>> queryPageList(Szph szph,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Szph>> result = new Result<IPage<Szph>>();
		QueryWrapper<Szph> queryWrapper = QueryGenerator.initQueryWrapper(szph, req.getParameterMap());
		queryWrapper.orderByAsc("ms");
		Page<Szph> page = new Page<Szph>(pageNo, pageSize);
		IPage<Szph> pageList = szphService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param szph
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Szph> add(@RequestBody Szph szph) {
		Result<Szph> result = new Result<Szph>();
		try {
			szphService.save(szph);
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
	 * @param szph
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Szph> edit(@RequestBody Szph szph) {
		Result<Szph> result = new Result<Szph>();
		Szph szphEntity = szphService.getById(szph.getId());
		if(szphEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = szphService.updateById(szph);
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
	public Result<Szph> delete(@RequestParam(name="id",required=true) String id) {
		Result<Szph> result = new Result<Szph>();
		Szph szph = szphService.getById(id);
		if(szph==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = szphService.removeById(id);
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
	public Result<Szph> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Szph> result = new Result<Szph>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.szphService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Szph> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Szph> result = new Result<Szph>();
		Szph szph = szphService.getById(id);
		if(szph==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(szph);
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
      QueryWrapper<Szph> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Szph szph = JSON.parseObject(deString, Szph.class);
              queryWrapper = QueryGenerator.initQueryWrapper(szph, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Szph> pageList = szphService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "项目收支平衡列表");
      mv.addObject(NormalExcelConstants.CLASS, Szph.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("项目收支平衡列表数据", "", "导出信息"));
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
          	  szphService.remove(new QueryWrapper<Szph>().eq("project_id",projectId));
              List<Szph> listSzphs = ExcelImportUtil.importExcel(file.getInputStream(), Szph.class, params);
              for (Szph szphExcel : listSzphs) {
              	  szphExcel.setProjectId(projectId);
                  szphService.save(szphExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listSzphs.size());
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
