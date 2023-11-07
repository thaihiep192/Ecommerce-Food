package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories" , uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
    @Column(name = "is_deleted")
    private boolean deleted;
    @Column(name = "is_activated")
    private boolean activated;
    public Category(String name){
        this.name = name;
        this.deleted = false;
        this.activated = true;
    }
}
