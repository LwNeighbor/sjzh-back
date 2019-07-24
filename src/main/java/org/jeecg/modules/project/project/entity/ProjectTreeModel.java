package org.jeecg.modules.project.project.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class ProjectTreeModel implements Serializable {


    private static final long serialVersionUID = 1L;

    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String title;

    private String sort;

    private boolean isLeaf;

    private String id;

    private String pid;

    private String projectName;

    private String projectDetail;

    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;

    private List<ProjectTreeModel> children = new ArrayList<>();


    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isleaf) {
        this.isLeaf = isleaf;
    }


    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param project
     */
    public ProjectTreeModel(Project project) {
        this.key = project.getId();
        this.value = project.getId();
        this.title = project.getProjectName();
        this.id = project.getId();
        this.pid = project.getPid();
        this.sort = project.getSort();
        this.projectDetail = project.getProjectDetail();

        this.createBy = project.getCreateBy();
        this.createTime = project.getCreateTime();
        this.updateBy = project.getUpdateBy();
        this.updateTime = project.getUpdateTime();
    }

    public List<ProjectTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectTreeModel> children) {
        if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
    }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectTreeModel model = (ProjectTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(pid, model.pid) &&
                Objects.equals(projectName, model.projectName) &&
                Objects.equals(projectDetail, model.projectDetail) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(children, model.children);
    }

}
