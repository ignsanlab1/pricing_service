package com.example.pricing_service.infraestructure.entity;

import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS", schema= "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull(message = "Name cannot be null")
    private String name;

    @Column(name = "user_id", nullable = false)
    @NotNull(message= "User ID cannot be null")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CategoryType category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PriceEntity> priceList;

    @Column(name = "hash_code", nullable = false, unique = true)
    private int hashCode;

    @PrePersist
    @PreUpdate
    public void calculateHashCode() {
        this.hashCode = Objects.hash(name, userId, category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userId, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductEntity that = (ProductEntity) obj;
        return Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId) &&
                category == that.category;
    }

}
