package br.com.fiap.station.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.station.model.Pedido;
import br.com.fiap.station.repository.PedidoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/station/pedido")
public class PedidoController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    PedidoRepository repo;

    @Autowired
    PagedResourcesAssembler<Pedido> assembler;

    @GetMapping
    @CrossOrigin
    public PagedModel<EntityModel<Pedido>> index(@PageableDefault(size = 5) Pageable pageable) {
        log.info("Buscando todos os pedidos!");

        Page<Pedido> page = repo.findAll(pageable);

        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @CrossOrigin
    public EntityModel<Pedido> show(@PathVariable Long id) {
        log.info("Buscando o pedido com o id: " + id);

        Pedido pedido = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado!"));

        return pedido.toModel();
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity<EntityModel<Pedido>> create(@RequestBody @Valid Pedido pedido, BindingResult result) {
        log.info("Cadastrando o pedido: " + pedido);

        repo.save(pedido);

        return ResponseEntity.created(pedido.toModel().getRequiredLink("self").toUri()).body(pedido.toModel());
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    public ResponseEntity<Pedido> destroy(@PathVariable Long id) {
        log.info("Deletando o pedido de id: " + id);

        Pedido pedido = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado!"));

        repo.delete(pedido);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CrossOrigin
    public ResponseEntity<Pedido> update(@PathVariable Long id, @RequestBody @Valid Pedido pedido, BindingResult result) {
        log.info("Editando pedido com id: " + id);

        var p = repo.findById(id);

        if (p.isEmpty()) return ResponseEntity.notFound().build();

        BeanUtils.copyProperties(pedido, p.get(), "id");

        repo.save(p.get());

        return ResponseEntity.ok(p.get());
    }
}
