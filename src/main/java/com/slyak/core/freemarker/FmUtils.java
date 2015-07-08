package com.slyak.core.freemarker;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
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
        return renderTpl(getTemplate(tpl), model);
    }

    public static String renderTpl(Template template, Object model) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            LOGGER.error("Template {0} not found", template.getName());
        } catch (TemplateException e) {
            LOGGER.error("Render template error", e);
        }
        return StringUtils.EMPTY;
    }

    public static Template getTemplate(String tpl) {
        try {
            return FmContext.cfg.getTemplate(tpl);
        } catch (IOException e) {
            LOGGER.error("Template {0} not found", tpl);
            return null;
        }
    }
}
