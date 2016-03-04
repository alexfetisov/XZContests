package org.xzteam.cpphelper.gen;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.xzteam.cpphelper.model.TaskId;

import java.util.HashMap;
import java.util.Map;

public class GenTask {
    private STGroup st = new STGroupFile("task.stg");
    private TaskId id;

    public GenTask() {
    }

    public GenTask setId(TaskId id) {
        this.id = id;
        return this;
    }

    public Map<String, String> gen() {
        Map<String, String> ret = new HashMap<>();
        ret.put("CMakeLists.txt", getTemplate("CMakeListstxt").render());
//        ret.put(dir + "main.cpp", getTemplate("maincpp").render());
//        ret.put(dir + "main.h", getTemplate("mainh").render());
//        ret.put(dir + "tests.cpp", getTemplate("testscpp").render());
        return ret;
    }

    private ST getTemplate(String templateName) {
        ST template = st.getInstanceOf(templateName);
        if (template.getAttributes() != null) {
            String prefix = id.getPrefix();
            if (template.getAttributes().containsKey("prefix")) {
                template.add("prefix", prefix);
            }
        }
        return template;
    }
}
