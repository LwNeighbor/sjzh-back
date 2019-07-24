package org.jeecg.modules.project.project.service;

import org.jeecg.modules.project.project.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.project.entity.ProjectTreeModel;

import java.util.List;

/**
 * @Description: 项目管理
 * @author： jeecg-boot
 * @date：   2019-07-15
 * @version： V1.0
 */
public interface IProjectService extends IService<Project> {

    List<ProjectTreeModel> queryTreeList();
}
