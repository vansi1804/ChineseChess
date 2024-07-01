package com.nvs.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training extends Auditor{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", updatable = false)
   private long id;

   @Column(name = "title")
   private String title;

   @ManyToOne(optional = true, fetch = FetchType.LAZY)
   @JoinColumn(name = "parent_training_id", referencedColumnName = "id")
   @ToString.Exclude
   private Training parentTraining;

   @OneToMany(mappedBy = "parentTraining", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
   @ToString.Exclude
   private List<Training> childTrainings = new ArrayList<>();

}
