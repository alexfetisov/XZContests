package org.xzteam.cpphelper.gen;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.xzteam.cpphelper.data.Problem;

import java.util.HashMap;
import java.util.Map;

public class GenTask {
    private STGroup st = new STGroupFile("task.stg");
    private Problem problem;

    public GenTask() {
    }

    public GenTask setProblem(Problem problem) {
        this.problem = problem;
        return this;
    }

    public Map<String, String> gen() {
        Map<String, String> ret = new HashMap<>();
        ret.put("CMakeLists.txt", getTemplate("CMakeListstxt").render());
        ret.put("main.cpp", getTemplate("maincpp").render());
        ret.put("main.h", getTemplate("mainh").render());
        ret.put("tests.cpp", getTemplate("testscpp").render());
        return ret;
    }

    private ST getTemplate(String templateName) {
        ST template = st.getInstanceOf(templateName);
        if (template.getAttributes() != null) {
            String prefix = problem.getPrefix();
            if (template.getAttributes().containsKey("prefix")) {
                template.add("prefix", prefix);
            }
            if (template.getAttributes().containsKey("samples")) {
                template.add("samples", problem.getSamples());
            }
        }
        return template;
    }
}
