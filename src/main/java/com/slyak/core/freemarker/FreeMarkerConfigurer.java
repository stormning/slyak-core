package com.slyak.core.freemarker;

import com.google.common.collect.Maps;
import com.slyak.core.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/5/8
 */
public class FreeMarkerConfigurer extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {

    private String implicitFile = "freemarker_implicit.ftl";

    private static final String FTLROOT_REGEX = "\\[#--\\s{1,}@ftlroot\\s{1,}\"(.*)\"\\s{1,}--\\]";

    private static final String IMPORT_REGEX = "\\[#import ['\"](.*)['\"] as (.*)\\]";

    @Override
    protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
        config.setObjectWrapper(new ConfigurableObjectWrapper());
        Resource resource = getResourceLoader().getResource("classpath:" + implicitFile);
        if (resource.exists()) {
            String ftlroot = "";
            Map<String, String> imports = Maps.newHashMap();
            List<String> lines = IOUtils.readLines(resource.getInputStream());
            for (String line : lines) {
                //find root
                List<String> rootVars = StringUtils.findGroupsIfMatch(FTLROOT_REGEX, line);
                if (CollectionUtils.isNotEmpty(rootVars)) {
                    ftlroot = rootVars.get(0);
                    continue;
                }

                List<String> importVars = StringUtils.findGroupsIfMatch(IMPORT_REGEX, line);
                if (CollectionUtils.isNotEmpty(importVars)) {
                    imports.put(importVars.get(1), importVars.get(0));
                    continue;
                }
            }
            if (!imports.isEmpty()) {
                for (Map.Entry<String, String> entry : imports.entrySet()) {
                    entry.setValue(ftlroot + entry.getValue());
                }
                config.setAutoImports(imports);
            }
        }
    }

    public void setImplicitFile(String implicitFile) {
        this.implicitFile = implicitFile;
    }

    public static void main(String[] args) {
//        [#import '/main.ftl' as main]
    }
}
