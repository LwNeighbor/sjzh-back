package org.jeecg.modules.project.project.utils;

import org.jeecg.modules.project.project.entity.Project;
import org.jeecg.modules.project.project.entity.ProjectIdModel;
import org.jeecg.modules.project.project.entity.ProjectTreeModel;

import java.util.ArrayList;
import java.util.List;

public class FindProjectListUtils {

    private static List<ProjectIdModel> idList = new ArrayList<>(4);


    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<ProjectTreeModel> wrapTreeDataToTreeList(List<Project> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        idList.clear();
        List<ProjectTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            Project depart = recordList.get(i);
            records.add(new ProjectTreeModel(recordList.get(i)));
        }
        List<ProjectTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }


    public static List<ProjectIdModel> wrapDepartIdModel() {
        return idList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<ProjectTreeModel> findChildren(List<ProjectTreeModel> recordList,
                                                         List<ProjectIdModel> idList) {

        List<ProjectTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            ProjectTreeModel branch = recordList.get(i);
            if ("".equals(branch.getPid())) {
                treeList.add(branch);
                ProjectIdModel departIdModel = new ProjectIdModel().convert(branch);
                idList.add(departIdModel);
            }
        }
        getGrandChildren(treeList,recordList,idList);
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<ProjectTreeModel> treeList,List<ProjectTreeModel> recordList,List<ProjectIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            ProjectTreeModel model = treeList.get(i);
            ProjectIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                ProjectTreeModel m = recordList.get(i1);
                if (m.getPid().equals(model.getId())) {
                    model.getChildren().add(m);
                    ProjectIdModel dim = new ProjectIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }


    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<ProjectTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            ProjectTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setIsLeaf(true);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
                model.setIsLeaf(false);
            }
        }
    }
}
