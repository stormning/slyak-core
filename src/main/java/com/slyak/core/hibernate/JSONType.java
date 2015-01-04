/*
 * Project:  slyak-core
 * Module:   slyak-core
 * File:     JSONType.java
 * Modifier: stormning
 * Modified: 2014-11-19 17:39
 * Copyright (c) 2014 Slyak All Rights Reserved.
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package com.slyak.core.hibernate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.type.descriptor.java.DataHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.jboss.logging.Logger;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class JSONType implements UserType, DynamicParameterizedType, Serializable {
    private static final long serialVersionUID = 352044032843534075L;
    private static final CoreMessageLogger LOG = (CoreMessageLogger) Logger.getMessageLogger(CoreMessageLogger.class, JSONType.class.getName());
    public static final String TYPE = "com.wisorg.scc.api.internal.core.support.hibernate.JSONType";
    public static final String CLASS_NAME = "class";
    private int sqlType = 12;
    private Type type = Object.class;

    public JSONType() {
    }

    public int[] sqlTypes() {
        return new int[]{this.sqlType};
    }

    public Class returnedClass() {
        return this.type instanceof ParameterizedType ? (Class) ((ParameterizedType) this.type).getRawType() : (Class) this.type;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equal(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String value = extractString(rs.getObject(names[0]));
        if (rs.wasNull()) {
            if (LOG.isTraceEnabled()) {
                LOG.tracev("Returning null as column {0}", names[0]);
            }

            return null;
        } else {
            return this.type == Object.class ? JSON.parse(value) : JSON.parseObject(value, this.type, new Feature[0]);
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            if (LOG.isTraceEnabled()) {
                LOG.tracev("Binding null to parameter: {0}", Integer.valueOf(index));
            }

            st.setNull(index, this.sqlType);
        } else {
            String json;
            if (this.type == Object.class) {
                json = JSON.toJSONString(value, new SerializerFeature[]{SerializerFeature.WriteClassName});
            } else {
                json = JSON.toJSONString(value);
            }

            if (this.sqlType == 2005) {
                StringReader sr = new StringReader(json);
                st.setCharacterStream(index, sr, json.length());
            } else {
                st.setObject(index, json, this.sqlType);
            }
        }

    }

    public Object deepCopy(Object value) throws HibernateException {
        return value instanceof JSONObject ? ((JSONObject) value).clone() : (value instanceof Cloneable ? ObjectUtils.clone(value) : (value instanceof Serializable ? SerializationHelper.clone((Serializable) value) : value));
    }

    public boolean isMutable() {
        return true;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public void setParameterValues(Properties parameters) {
        try {
            Class aClass = ReflectHelper.classForName(parameters.getProperty("org.hibernate.type.ParameterType.entityClass"));
            Field field = ReflectionUtils.findField(aClass, parameters.getProperty("org.hibernate.type.ParameterType.propertyName"));
            Type fieldType = field.getGenericType();
            if (fieldType instanceof Class || fieldType instanceof ParameterizedType) {
                this.type = fieldType;
            }

            this.parseSqlType(field.getAnnotations());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            ParameterType reader = (ParameterType) parameters.get("org.hibernate.type.ParameterType");
            if (reader != null) {
                this.type = reader.getReturnedClass();
                this.parseSqlType(reader.getAnnotationsMethod());
            } else {
                try {
                    this.type = ReflectHelper.classForName((String) parameters.get("class"));
                } catch (ClassNotFoundException ce) {
                    throw new HibernateException("class not found", ce);
                }
            }

        }
    }

    private void parseSqlType(Annotation[] anns) {
        int len = anns.length;
        for (int i = 0; i < len; ++i) {
            Annotation an = anns[i];
            if (an instanceof Column) {
                int length = ((Column) an).length();
                if (length > 4000) {
                    this.sqlType = 2005;
                }
                break;
            }
        }

    }

    private static String extractString(Object value) {
        return value == null ? null : (value instanceof String ? (String) value : (value instanceof Reader ? DataHelper.extractString((Reader) value) : (value instanceof Clob ? DataHelper.extractString((Clob) value) : null)));
    }
}
