package org.example.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;

    @Column
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;

    @Column
    private boolean admin;

    public Usuario(String login){
        this.login = login;
    }

    public Usuario(String login,String senha){
        this.senha =senha;
        this.login = login;
    }


}
