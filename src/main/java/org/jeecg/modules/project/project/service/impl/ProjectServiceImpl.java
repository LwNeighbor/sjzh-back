package org.jeecg.modules.project.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.project.project.entity.Project;
import org.jeecg.modules.project.project.entity.ProjectTreeModel;
import org.jeecg.modules.project.project.mapper.ProjectMapper;
import org.jeecg.modules.project.project.service.IProjectService;
import org.jeecg.modules.project.project.utils.FindProjectListUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 项目管理
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Override
    public List<ProjectTreeModel> queryTreeList() {
        List<Project> list = this.list();
        // 调用wrapTreeDataToTreeList方法生成树状数据
        List<ProjectTreeModel> listResult = FindProjectListUtils.wrapTreeDataToTreeList(list);
        return listResult;
    }
}
