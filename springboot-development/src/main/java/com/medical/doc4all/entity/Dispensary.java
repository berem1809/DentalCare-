// entity/Dispensary.java
package com.medical.doc4all.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dispensary")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dispensary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // Link to User in BFF

    private String name;

    private String address;
    private Double longitude;
    private Double latitude;

    private Float rating; // Average rating from patients
    private String Description;

    private String contactNumber;
    private String website;
    @Enumerated(EnumType.STRING)
    private DispensaryType type;

    private Integer reviewCount;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "dispensary", cascade = CascadeType.ALL)
    private List<DoctorInvitation> invitations = new ArrayList<>();

}