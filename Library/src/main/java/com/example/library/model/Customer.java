package com.example.library.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String address;
    private String password;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name", referencedColumnName = "id")
    private City city;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="country_id", referencedColumnName = "country_id")
    private Country country;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;
    @OneToMany(mappedBy = "customer",cascade =  CascadeType.ALL)
    private List<Order> orders;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "customers_roles",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;
    public Customer(){
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }
}
