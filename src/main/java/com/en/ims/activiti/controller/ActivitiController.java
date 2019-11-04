package com.en.ims.activiti.controller;

import com.en.ims.activiti.util.DefaultProcessDiagramGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author 王海恩
 * @Date 2019/9/19
 **/
@RestController
@RequestMapping("/activiti")
@Slf4j
public class ActivitiController {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 创建模型
     */
    @RequestMapping("/create")
    public void create(HttpServletRequest request, HttpServletResponse response) {
        try {
            String description = "新建工作流";
            String modelName = "新建工作流";
            String key = "123456";
            RepositoryService repositoryService = processEngine.getRepositoryService();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(modelName);
            modelData.setKey(key);
            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {
            log.error("创建模型失败!");
        }
    }
    /**     * 获取所有模型     */
    @RequestMapping("/list")
    public Object modelList(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        return repositoryService.createModelQuery().list();
    }
    /**
     * 删除模型
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public String deleteModel(@PathVariable("id")String id){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteModel(id);
        return "删除成功";
    }

    /**
     * 发布模型为流程定义
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/deployment/{id}")
    public String deploy(@PathVariable("id")String id) throws Exception {
        //获取模型
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Model modelData = repositoryService.getModel(id);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

        if (bytes == null) {
            return ("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }

        JsonNode modelNode = new ObjectMapper().readTree(bytes);

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if(model.getProcesses().size()==0){
            return ("数据模型不符要求，请至少设计一条主线流程。");
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        //发布流程
        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
        return "发布成功";
    }
    @RequestMapping("/start/{id}")
    public String start(@PathVariable("id") String id){
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        ProcessInstance myProcess_1 = runtimeService.startProcessInstanceByKey("bijj");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(id);
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("流程定义ID:" + processInstance.getProcessDefinitionId());
        List<Task> list = processEngine.getTaskService().createTaskQuery().list();
        for(Task  task :list){
            if(task.getProcessInstanceId().equals(processInstance.getId())){
                System.out.println("任务id："+task.getId());
            }
        }
        return processInstance.getId();
    }
    @RequestMapping("/complete")
    public void complete(){
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("flag",1);
        taskService.complete("25131",variables);
    }

    @RequestMapping("history/{processInstanceId}")
    public void processTracking(@PathVariable("processInstanceId") String processInstanceId,HttpServletResponse response) throws Exception {
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String procDefId = processInstance.getProcessDefinitionId();
        // 当前活动节点、活动线
        List<String> activeActivityIds = new ArrayList<>(), highLightedFlows;
        //所有的历史活动节点
        List<String> highLightedFinishes = new ArrayList<>();
        // 如果流程已经结束，则得到结束节点
        if (processEngine.getHistoryService().createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() == 0) {
            // 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            activeActivityIds = processEngine.getRuntimeService().getActiveActivityIds(processInstanceId);
        }
        // 获得历史活动记录实体（通过启动时间正序排序，不然有的线可以绘制不出来）
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();

        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            highLightedFinishes.add(historicActivityInstance.getActivityId());
        }
        // 计算活动线
        highLightedFlows = getHighLightedFlows((ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService())
                .getDeployedProcessDefinition(procDefId),historicActivityInstances);
        if (null != activeActivityIds) {
            InputStream imageStream = null;
            try {
                response.setContentType("image/png");
                // 获得流程引擎配置
                ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
                // 根据流程定义ID获得BpmnModel
                BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(procDefId);
                // 输出资源内容到相应对象
                imageStream = new DefaultProcessDiagramGenerator().generateDiagram(
                        bpmnModel,
                        "png",
                        highLightedFinishes,//所有活动过的节点，包括当前在激活状态下的节点
                        activeActivityIds,//当前为激活状态下的节点
                        highLightedFlows,//活动过的线
                        "宋体",
                        "宋体",
                        "宋体",
                        processEngineConfiguration.getClassLoader(),
                        1.0);
                int len;
                byte[] b = new byte[1024];
                while ((len = imageStream.read(b, 0, 1024)) != -1) {
                    response.getOutputStream().write(b, 0, len);
                }
            } finally {
                if(imageStream != null){
                    imageStream.close();
                }
            }
        }
    }

    /**
     * @title getHighLightedFlows
     * @description: 获取流程应该高亮的线
     * @param processDefinitionEntity 流程定义实例
     * @param historicActivityInstances 流程活动节点实例
     * @return: java.util.List<java.lang.String>
     */
    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<>();// 用以保存高亮的线flowId
        List<String> highActivitiImpl = new ArrayList<>();
        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            highActivitiImpl.add(historicActivityInstance.getActivityId());
        }
        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstance.getActivityId());
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
            // 对所有的线进行遍历
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (highActivitiImpl.contains(pvmActivityImpl.getId())) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
}