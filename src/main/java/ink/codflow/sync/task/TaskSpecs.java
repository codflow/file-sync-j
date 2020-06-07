package ink.codflow.sync.task;

import java.util.HashMap;
import java.util.Map;

import ink.codflow.sync.consts.TaskSpecType;

public class TaskSpecs {

    Map<TaskSpecType, String> map = new HashMap<TaskSpecType, String>();

    public void addSpec(TaskSpecType type, String value) {
        map.put(type, value);
    }

    public String getSpec(TaskSpecType type) {
        return map.get(type);
    }

}
