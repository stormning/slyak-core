package com.slyak.core.freemarker;

import com.google.common.collect.Maps;
import com.slyak.core.StringUtils;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.util.ClassUtils;

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
    private static final String IMPORT_REGEX = "\\[#import\\s{1,}['\"](.*)['\"]\\s{1,}as\\s{1,}(.*)\\]";
    private static final String VARIABLE_REGEX = "\\[#--\\s{1,}@ftlvariable\\s{1,}name=\"(.*)\"\\s{1,}type=\"(.*)\"\\s{1,}--]";

    private String implicitFile = "freemarker_implicit.ftl";

    private String ftlroot;
    private Map<String, String> imports = Maps.newHashMap();
    private Map<String, String> variables = Maps.newHashMap();


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

                    if (fillVariableMap(line, IMPORT_REGEX, imports)) {
                        continue;
                    }

                    fillVariableMap(line, VARIABLE_REGEX, variables);
                }
                if (ftlroot != null) {
                    templateLoaders.add(new SpringTemplateLoader(getResourceLoader(), ResourceLoader.CLASSPATH_URL_PREFIX + ftlroot));
                }
            }
        } catch (Exception e) {
            //ignore?
        }

    }

    private boolean fillVariableMap(String line, String regex, Map<String, String> vmap) {
        List<String> variableVars = StringUtils.findGroupsIfMatch(regex, line);
        if (CollectionUtils.isNotEmpty(variableVars)) {
            vmap.put(variableVars.get(0), variableVars.get(1));
            return true;
        }
        return false;
    }

    @Override
    protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
        config.setObjectWrapper(new ConfigurableObjectWrapper());
        if (!imports.isEmpty()) {
            for (Map.Entry<String, String> iptEntry : imports.entrySet()) {
                config.addAutoImport(iptEntry.getKey(), iptEntry.getValue());
            }
        }
        if (!variables.isEmpty()) {
            for (Map.Entry<String, String> valEntry : variables.entrySet()) {
                String vname = valEntry.getKey();
                if (config.getSharedVariableNames().contains(vname)) {
                    continue;
                }
                String val = valEntry.getValue();
                try {
                    Class<?> aClass = ClassUtils.forName(val, ClassUtils.getDefaultClassLoader());
                    if (TemplateModel.class.isAssignableFrom(aClass)) {
                        config.setSharedVariable(vname, BeanUtils.instantiate(aClass));
                    } else {
                        Controller ctlAnn = AnnotationUtils.findAnnotation(aClass, Controller.class);
                        if (ctlAnn == null) {
                            //must be static methods

                        } else {
                            config.setSharedVariable(vname, new ControllerModel(aClass));
                        }
                    }

                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    public void setImplicitFile(String implicitFile) {
        this.implicitFile = implicitFile;
    }
}
