package uz.pdp.springsecuritydatabseemailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class) // CreatedBy va CreatedBy kim tomondanligini bilish uchun shu annotation ni qo'shdik , bu entity ni listen qiladi.
public class Product {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String description;
    @CreatedBy // User lardan kim qo'shganini bilish uchun , CreatedBy qo'ymasak foydasi yo'q.
    private UUID createdBy;
    @LastModifiedBy // User lardan kim oxirgi marta o'zgartirganini bilish uchun , LastModifiedBy qo'ymasak foydasi yo'q.
    private UUID updatedBy;
    @CreationTimestamp // yaratilgan vaqti
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
