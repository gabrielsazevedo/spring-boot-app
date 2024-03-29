package programacao.web.controller;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import programacao.web.model.Usuario;
import programacao.web.repository.UsuarioRepository;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository ur;

    @RequestMapping(value = { "/cadastrarUsuario", "/editarUsuario" }, method = RequestMethod.GET)
    public String exibirForm(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "formUsuario";

    }

    @RequestMapping(value = "/cadastrarUsuario", method = RequestMethod.POST)
    public String processarFormCadastro(Usuario usuario, Model model) {

        try {

            verificarUsuarioCadastro(usuario);

        } catch (Exception e) {

            model.addAttribute("erro", e.getMessage());
            return "formUsuario";

        }

        ur.save(usuario);

        return "redirect:/cadastrarUsuario";

    }

    @RequestMapping(value = "/editarUsuario", method = RequestMethod.POST)
    public String processarFormEdicao(Usuario usuario, Model model) throws Excecao {

        Usuario usuarioExistente = ur.findByLogin(usuario.getLogin());

        if (usuarioExistente != null) {

            usuarioExistente.setNome(usuario.getNome());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setSenha(usuario.getSenha());

            try {

                verificarUsuarioEdicao(usuario);

            } catch (Exception e) {

                model.addAttribute("erro", e.getMessage());
                return "formUsuario";

            }

            ur.save(usuarioExistente);

        } else {

            model.addAttribute("erro", "Usuário não encontrado.");
            return "formUsuario";

        }

        return "redirect:/editarUsuario";

    }

    @RequestMapping(value = "/removerUsuario", method = RequestMethod.GET)
    public String exibirFormRemocao(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "formRemocao";

    }

    @RequestMapping(value = "/removerUsuario", method = RequestMethod.POST)
    public String processarFormRemocao(Usuario usuario, Model model) throws Excecao {

        Usuario usuarioExistente = ur.findByLogin(usuario.getLogin());

        if (usuarioExistente != null) {

            ur.delete(usuarioExistente);

        } else {

            model.addAttribute("erro", "Usuário não encontrado.");
            return "formRemocao";

        }

        return "redirect:/removerUsuario";

    }

    @RequestMapping("/listarUsuario")
    public String listarUsuario(Model model) {

        Iterable<Usuario> usuarios = ur.findAll();
        model.addAttribute("usuarios", usuarios);

        return "listarUsuario";

    }

    public void verificarUsuarioCadastro(Usuario usuario) throws Excecao {

        List<String> mensagensExcecao = new ArrayList<>();
        Usuario usuarioExistente = ur.findByLogin(usuario.getLogin());

        if (usuarioExistente != null) {

            mensagensExcecao.add("Usuário inválido. Já existe um usuário com o mesmo login.");

        }

        if (!usuario.getEmail().equals(usuario.getEmailconfirma())) {

            mensagensExcecao.add("E-mails não coincidem.");

        }

        if (usuario.getSenha().length() < 4 || usuario.getSenha().length() > 8) {

            mensagensExcecao.add("Sua senha deve ter um mínimo de 4 caracteres e um máximo de 8 caracteres.");

        }

        if (usuario.getSenha().equals(usuario.getLogin())) {

            mensagensExcecao.add("A senha não pode ser igual ao login.");

        }

        if (!usuario.getSenha().equals(usuario.getSenhaconfirma())) {

            mensagensExcecao.add("Senhas não coincidem.");

        }

        if (!mensagensExcecao.isEmpty()) {

            throw new Excecao(String.join(" ", mensagensExcecao));

        }
    }

    public void verificarUsuarioEdicao(Usuario usuario) throws Excecao {

        List<String> mensagensExcecao = new ArrayList<>();

        if (!usuario.getEmail().equals(usuario.getEmailconfirma())) {

            mensagensExcecao.add("E-mails não coincidem.");

        }

        if (usuario.getSenha().length() < 4 || usuario.getSenha().length() > 8) {

            mensagensExcecao.add("Sua senha deve ter um mínimo de 4 caracteres e um máximo de 8 caracteres.");

        }

        if (usuario.getSenha().equals(usuario.getLogin())) {

            mensagensExcecao.add("A senha não pode ser igual ao login.");

        }

        if (!usuario.getSenha().equals(usuario.getSenhaconfirma())) {

            mensagensExcecao.add("Senhas não coincidem.");

        }

        if (!mensagensExcecao.isEmpty()) {

            throw new Excecao(String.join(" ", mensagensExcecao));

        }

    }

}
