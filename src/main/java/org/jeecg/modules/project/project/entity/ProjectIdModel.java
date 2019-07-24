package org.jeecg.modules.project.project.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectIdModel implements Serializable {
    private static final long serialVersionUID = 1L;

    // 主键ID
    private String key;

    // 主键ID
    private String value;

    // 部门名称
    private String title;



    List<ProjectIdModel> children = new ArrayList<>();

    /**
     * 将SysDepartTreeModel的部分数据放在该对象当中
     * @param treeModel
     * @return
     */
    public ProjectIdModel convert(ProjectTreeModel treeModel) {
        this.key = treeModel.getId();
        this.value = treeModel.getId();
        this.title = treeModel.getProjectName();
        return this;
    }

    /**
     * 该方法为用户部门的实现类所使用
     * @param project
     * @return
     */
    public ProjectIdModel convertProject(Project project) {
        this.key = project.getId();
        this.value = project.getId();
        this.title = project.getProjectName();
        return this;
    }

    public List<ProjectIdModel> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectIdModel> children) {
        this.children = children;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
