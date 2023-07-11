package com.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training extends Auditing{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "title", columnDefinition = "nvarchar(255)")
    private String title;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_training_id", referencedColumnName = "id")
    private Training parentTraining;

    @OneToMany(mappedBy = "parentTraining")
    private List<Training> childTrainings = new ArrayList<>();

}
