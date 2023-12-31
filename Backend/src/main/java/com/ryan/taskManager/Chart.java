package com.ryan.taskManager;

import java.util.Date;
import java.util.List;

import javax.annotation.processing.Generated;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
public class Chart {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "workspace_ID", referencedColumnName = "ID", nullable = false)
    private Workspace workspaceID;

    @Column(nullable = false)
    private String name;

    @Column(name="created_date")
    @CreationTimestamp
    private Date createdDate;

    @OneToMany(mappedBy = "chartID", cascade = CascadeType.ALL)
    private List<Item> items;

    // GETTERS
    public Integer getID() {
        return ID;
    }

    public Integer getWorkspaceID() {
        return workspaceID.getID();
    }

    public String getName() {
        return name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public List<Item> getItems() {
        return items;
    }
    
    // SETTERS
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setWorkspaceID(Workspace workspace) {
        this.workspaceID = workspace;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
