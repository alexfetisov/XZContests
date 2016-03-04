package org.xzteam.cpphelper.gen;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenProject {
    private STGroup st = new STGroupFile("project.stg");
    private String name;
    private List<String> includes;

    public GenProject() {
    }

    public GenProject setName(String name) {
        this.name = name;
        return this;
    }

    public GenProject setIncludes(List<String> includes) {
        this.includes = includes;
        return this;
    }

    public Map<String, String> gen() {
        Map<String, String> ret = new HashMap<>();
        ret.put("CMakeLists.txt", getTemplate("rootCMakeLists").render());
        ret.put("cmake/gtest.cmake", getTemplate("gtestCMake").render());
        ret.put("lib/prelude.h", getTemplate("preludeh").render());
        ret.put("lib/test_util.h", getTemplate("test_utilh").render());
        return ret;
    }

    private ST getTemplate(String templateName) {
        ST template = st.getInstanceOf(templateName);
        if (template.getAttributes() != null) {
            if (template.getAttributes().containsKey("name")) {
                template.add("name", name);
            }
            if (template.getAttributes().containsKey("includes")) {
                template.add("includes", includes);
            }
        }
        return template;
    }
}
