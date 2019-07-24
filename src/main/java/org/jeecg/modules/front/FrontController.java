package org.jeecg.modules.front;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.project.cw.entity.Cw;
import org.jeecg.modules.project.cw.service.ICwService;
import org.jeecg.modules.project.detail.entity.Detail;
import org.jeecg.modules.project.detail.service.IDetailService;
import org.jeecg.modules.project.jd.entity.Jd;
import org.jeecg.modules.project.jd.service.IJdService;
import org.jeecg.modules.project.project.entity.Project;
import org.jeecg.modules.project.project.service.IProjectService;
import org.jeecg.modules.project.szph.entity.Szph;
import org.jeecg.modules.project.szph.service.ISzphService;
import org.jeecg.modules.project.zhuser.entity.ZhUser;
import org.jeecg.modules.project.zhuser.service.IZhUserService;
import org.jeecg.modules.project.zlbt.entity.Bt;
import org.jeecg.modules.project.zlbt.service.IBtService;
import org.jeecg.modules.project.zlzx.entity.Zx;
import org.jeecg.modules.project.zlzx.service.IZxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/front")
public class FrontController{


    @Autowired
    private IZhUserService userService;
    @Autowired
    private IDetailService detailService;
    @Autowired
    private IBtService btService;
    @Autowired
    private IZxService zxService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IJdService jdService;
    @Autowired
    private ICwService cwService;
    @Autowired
    private ISzphService szphService;

    /**
     * 登陆
     * @param
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation("登录接口")
    public Result<JSONObject> login(@RequestParam("username") String username,
                                    @RequestParam("password") String password) {
        Result<JSONObject> result = new Result<JSONObject>();


        JSONObject jsonObject = new JSONObject();
        ZhUser one = userService.getOne(new QueryWrapper<ZhUser>().eq("username", username).
                eq("password", password));

        if(one != null){
            jsonObject.put("id",one.getId());
            result.setResult(jsonObject);
            result.success("登录成功");
        }else {
            result.error500("账号名或密码错误");
        }
        return result;
    }


    /**
     * 修改密码
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ApiOperation("修改密码")
    public Result<JSONObject> updatePwd(@RequestParam("username") String username,
                                    @RequestParam("oldpwd") String oldpwd,
                                    @RequestParam("newpwd") String newpwd,
                                    @RequestParam("confirmpwd") String confirmpwd) {
        Result<JSONObject> result = new Result<JSONObject>();


        JSONObject jsonObject = new JSONObject();


        if(!newpwd.equalsIgnoreCase(confirmpwd)){
            result.error500("新密码与确认密码不相同,请修改");
            return result;
        }

        ZhUser one = userService.getOne(new QueryWrapper<ZhUser>().eq("username", username).
                eq("password", oldpwd));

        if(one != null){

            one.setPassword(newpwd);
            userService.updateById(one);
            result.success("修改成功");
        }else {
            result.error500("账号名或密码错误");
        }
        return result;
    }


    /**
     * 使用说明
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/show", method = RequestMethod.POST)
    @ApiOperation("使用说明")
    public Result<JSONObject> show() {
        Result<JSONObject> result = new Result<JSONObject>();

        JSONObject jsonObject = new JSONObject();

        Detail detail = new Detail();

        List<Detail> list = detailService.list();
        if(list.size() > 0){
            detail = list.get(0);
        }

        jsonObject.put("detail",detail);
        result.setResult(jsonObject);
        result.success("操作成功");

        return result;
    }

    /**
     * 首页图及时间查询
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @ApiOperation("首页图及时间查询")
    public Result<JSONObject> home(@RequestParam("id") String id,
                                       @RequestParam(value = "time",defaultValue = "2019",required = false) String time) {

        Result<JSONObject> result = new Result<JSONObject>();

        JSONObject jsonObject = new JSONObject();

        Map<String,List<String>> zray = new HashMap<>();
        List<String> xray = new ArrayList<>();
        List<String> yray = new ArrayList<>();
        ZhUser user = userService.getById(id);
        if(user == null){
            result.error9999();
            return result;
        }

        List<Bt> bts = btService.list(new QueryWrapper<Bt>().
                select("percent","is_done").
                eq("years", time));

        List<Zx> zxs = zxService.list(new QueryWrapper<Zx>().
                select("xray","yray").
                eq("years", time).orderByAsc("sort"));


        if(zxs.size()  > 0 ){
            zxs.stream().forEach(zx -> {
                xray.add(zx.getXray());
                yray.add(zx.getYray());
            });
        }

        zray.put("xray",xray);
        zray.put("yray",yray);
        jsonObject.put("bt",bts);
        jsonObject.put("zx",zray);
        result.setResult(jsonObject);
        result.success("操作成功");
        return result;
    }


    /**
     * 首页时间搜索的时间元素
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/timeList", method = RequestMethod.POST)
    @ApiOperation("首页时间搜索的时间元素")
    public Result<JSONObject> timeList(@RequestParam("id") String id) {
        Result<JSONObject> result = new Result<JSONObject>();

        JSONObject jsonObject = new JSONObject();


        ZhUser user = userService.getById(id);
        if(user == null){
            result.error9999();
            return result;
        }


        List<Bt> list = btService.list(new QueryWrapper<Bt>().select("years").orderByAsc("years").groupBy("years"));

        jsonObject.put("time",list);
        result.setResult(jsonObject);
        result.success("操作成功！");
        return result;
    }



    /**
     * 项目列表
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/projectList", method = RequestMethod.POST)
    @ApiOperation("项目列表")
    public Result<JSONObject> projectList(@RequestParam("id") String id) {
        Result<JSONObject> result = new Result<JSONObject>();

        JSONObject jsonObject = new JSONObject();


        ZhUser user = userService.getById(id);
        if(user == null){
            result.error9999();
            return result;
        }

        List<Project> list = projectService.list(new QueryWrapper<Project>().
                select("id","project_name", "create_time").
                eq("pid","").orderByAsc("sort"));

        jsonObject.put("project",list);
        result.setResult(jsonObject);
        result.success("操作成功");

        return result;
    }


    /**
     * 项目搜索
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation("项目搜索")
    public Result<JSONObject> search(@RequestParam("id") String id,
                                     @RequestParam("name") String name) {
        Result<JSONObject> result = new Result<JSONObject>();

        JSONObject jsonObject = new JSONObject();


        ZhUser user = userService.getById(id);
        if(user == null){
            result.error9999();
            return result;
        }

        List<Project> list = projectService.list(new QueryWrapper<Project>().
                select("id","project_name", "create_time").like("project_name",name).
                eq("pid","").orderByAsc("sort"));

        jsonObject.put("project",list);
        result.setResult(jsonObject);
        result.success("操作成功");

        return result;
    }


    /**
     * 指定项目详情
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/projectById", method = RequestMethod.POST)
    @ApiOperation("指定项目详情")
    public Result<JSONObject> projectById(@RequestParam("id") String id,
                                          @RequestParam("projectId") String projectId) {
        Result<JSONObject> result = new Result<JSONObject>();

        JSONObject jsonObject = new JSONObject();


        Project project = projectService.getById(projectId);

        ZhUser user = userService.getById(id);
        if(user == null){
            result.error9999();
            return result;
        }

        List<String> jdxray = new ArrayList<>();    //x轴
        List<String> jdmray = new ArrayList<>();    //目标额度
        List<String> jdpray = new ArrayList<>();    //实际额度

        List<String> cwxray = new ArrayList<>();    //x轴
        List<String> cwyray = new ArrayList<>();    //y轴

        List<String> szphxray = new ArrayList<>();    //收入
        List<String> sryray = new ArrayList<>();    //收入y
        List<String> zcyray = new ArrayList<>();    //支出y
        List<String> ylyray = new ArrayList<>();    //盈利y


        List<Project> projects = projectService.list(new QueryWrapper<Project>().eq("pid", projectId));

        if(projects.size() > 0){
            //说明有子项目
            List<Project> list = projectService.list(new QueryWrapper<Project>().
                    select("id","project_name", "create_time").
                    eq("pid",projectId).orderByAsc("sort"));

            jsonObject.put("project",list);

        }else if(projects.size() == 0){
            jsonObject.put("detail",project.getProjectDetail());    //项目介绍
            jsonObject.put("title",project.getProjectName());       //项目标题
        }

        jsonObject.put("count",projects.size());



        List<Jd> jdList = jdService.list(new QueryWrapper<Jd>().select("ms", "percent","number").
                eq("project_id", projectId));


        jdList.stream().forEach(jd -> {
            jdxray.add(jd.getMs());
            jdmray.add(jd.getNumber());     //目标
            jdpray.add(jd.getPercent());   //实际
        });

        jsonObject.put("jdxray",jdxray);
        jsonObject.put("jdmray",jdmray);
        jsonObject.put("jdpray",jdpray);


        List<Cw> cwList = cwService.list(new QueryWrapper<Cw>().select("ms", "number").
                eq("project_id", projectId));


        cwList.stream().forEach(cw -> {
            cwxray.add(cw.getMs());
            cwyray.add(cw.getNumber());     //目标
        });


        jsonObject.put("cwxray",cwxray);
        jsonObject.put("cwyray",cwyray);


        List<Szph> szphList = szphService.list(new QueryWrapper<Szph>().select("ms", "spercent","zpercent","ypercent").
                eq("project_id", projectId).orderByAsc("ms"));


        szphList.stream().forEach(szph -> {
            szphxray.add(szph.getMs());
            sryray.add(szph.getSpercent());     //收入
            zcyray.add(szph.getZpercent());     //支出
            ylyray.add(szph.getYpercent());     //盈利
        });

        jsonObject.put("szphxray",szphxray);
        jsonObject.put("sryray",sryray);
        jsonObject.put("zcyray",zcyray);
        jsonObject.put("ylyray",ylyray);


        result.setResult(jsonObject);
        result.success("操作成功");

        return result;
    }
}
