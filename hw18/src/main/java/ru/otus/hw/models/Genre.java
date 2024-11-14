package ru.otus.hw.models;

import com.fasterxml.jackson.annotation.JsonInclude;

//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@JsonInclude(JsonInclude.Include.NON_ABSENT)
//@Entity
@Table(name = "genres", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(value = "name")
    private String name;

}
