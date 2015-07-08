package com.slyak.bean;

import com.slyak.core.hibernate.JSONType;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/2/2
 */
@MappedSuperclass
public abstract class Summary extends AuditableBase implements Serializable,Bizable {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String summary;

    @Column
    @Type(type = JSONType.TYPE)
    private List<Long> fileIds;

    @Column
    private Status status = Status.ENABLED;

    @Column(nullable = false)
    private int weight = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Long> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<Long> fileIds) {
        this.fileIds = fileIds;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
