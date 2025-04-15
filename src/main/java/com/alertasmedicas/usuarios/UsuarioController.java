package com.alertasmedicas.usuarios;


import com.alertasmedicas.usuarios.dao.UsuarioDAO;
import com.alertasmedicas.usuarios.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return UsuarioDAO.crearUsuario(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return UsuarioDAO.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable String id) {
        return UsuarioDAO.obtenerUsuario(id);
    }

    @PutMapping
    public Usuario actualizarUsuario(@RequestBody Usuario usuario) {
        return UsuarioDAO.actualizarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public boolean eliminarUsuario(@PathVariable String id) {
        return UsuarioDAO.eliminarUsuario(id);
    }
}
