package com.slyak.core.freemarker;

import com.google.common.collect.Maps;
import com.slyak.core.StringUtils;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;

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

    private static final String FTLROOT_REGEX = "\\[#--\\s{1,}@ftlroot\\s{1,}\"(.*)\"\\s{1,}--\\]";
    private static final String IMPORT_REGEX = "\\[#import ['\"](.*)['\"] as (.*)\\]";

    private String implicitFile = "freemarker_implicit.ftl";

    private String ftlroot;
    private Map<String, String> imports = Maps.newHashMap();
    private Map<String, Object> variables = Maps.newHashMap();


    @Override
    protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
        super.postProcessTemplateLoaders(templateLoaders);
        try {
            Resource resource = getResourceLoader().getResource(ResourceLoader.CLASSPATH_URL_PREFIX + implicitFile);
            if (resource.exists()) {
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

                    //TODO find variables
                }
                if (ftlroot != null) {
                    templateLoaders.add(new SpringTemplateLoader(getResourceLoader(), ResourceLoader.CLASSPATH_URL_PREFIX + ftlroot));
                }
            }
        } catch (Exception e) {
            //ignore?
        }

    }

    @Override
    protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
        config.setObjectWrapper(new ConfigurableObjectWrapper());
        if (!imports.isEmpty()) {
            config.setAutoImports(imports);
        }
    }

    public void setImplicitFile(String implicitFile) {
        this.implicitFile = implicitFile;
    }
}
