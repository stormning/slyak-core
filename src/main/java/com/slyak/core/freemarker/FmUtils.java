package com.slyak.core.freemarker;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/7/7
 */
public class FmUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FmUtils.class);

    public static String renderTpl(String tpl, Object model) {
        try {
            Template template = FmContext.cfg.getTemplate(tpl);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (TemplateException e) {
            LOGGER.error("template error", e);
        } catch (IOException e) {
            LOGGER.error("template not found", e);
        }
        return null;
    }
}
