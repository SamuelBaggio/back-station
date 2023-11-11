package br.com.fiap.station.model;

import org.springframework.hateoas.EntityModel;

import br.com.fiap.station.controller.UsuarioController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.data.domain.Pageable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "tb_usuario",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_email_usuario",
            columnNames = "email_usuario"
        ),
        @UniqueConstraint(
            name = "uk_cpf_usuario",
            columnNames = "cpf_usuario"
        )
    }
)
public class Usuario extends EntityModel<Usuario> {
    @Id
    @GeneratedValue(
        generator = "seq_usuario",
        strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
        name = "seq_usuario",
        sequenceName = "seq_usuario",
        allocationSize = 1
    )
    @Column(name = "id_usuario")
    private Long id;

    @Email
    @Column(name = "email_usuario")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z].* [A-Z][a-z]+$")
    @Column(name = "nome_usuario")
    private String nome;

    @NotBlank
    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}$")
    @Column(name = "cpf_usuario")
    private String cpf;

    @NotBlank
    @Size(min = 8)
    @Column(name = "senha_usuario")
    private String senha;

    public Usuario() {}

    public Usuario(Long id, String email, String nome, String cpf, String senha) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
    }

    public Usuario(String email, String nome, String cpf, String senha) {
        this.email = email;
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
    }

    public EntityModel<Usuario> toModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(UsuarioController.class).show(id)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(UsuarioController.class).index(Pageable.unpaged(), null)).withRel("listAll")
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", email=" + email + ", nome=" + nome + ", cpf=" + cpf + ", senha=" + senha + "]";
    }
}
