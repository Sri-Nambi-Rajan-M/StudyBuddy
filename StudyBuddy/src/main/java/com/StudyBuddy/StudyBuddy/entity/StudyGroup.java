package com.StudyBuddy.StudyBuddy.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_groups")
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private Student creator;

    @Column(nullable = false)
    private Integer maxMembers;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    public StudyGroup() {}

    public StudyGroup(String name, Subject subject, Student creator, Integer maxMembers) {
        this.name = name;
        this.subject = subject;
        this.creator = creator;
        this.maxMembers = maxMembers;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Subject getSubject() { return subject; }
    public Student getCreator() { return creator; }
    public Integer getMaxMembers() { return maxMembers; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Membership> getMemberships() { return memberships; }
}