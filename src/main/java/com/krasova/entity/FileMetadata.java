package com.krasova.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by osamo on 3/23/2017.
 */
@Data
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FileMetadata {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "category", length = 255)
    private String category;
    @Column(name = "description", length = 255)
    private String description;
    @Column(name = "uploadDate", length = 255)
    private Date uploadDate;
}
