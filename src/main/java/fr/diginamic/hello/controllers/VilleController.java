package fr.diginamic.hello.controllers;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.exceptions.RequeteIncorrecteException;
import fr.diginamic.hello.exceptions.RessourceExistanteException;
import fr.diginamic.hello.exceptions.RessourceNotFoundException;
import fr.diginamic.hello.mappers.VilleMapper;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VilleController {
    @Autowired
    private VilleService villeService;

    @GetMapping("villes/liste")
    public String getVilles(Model model) throws RessourceNotFoundException {
        List<Ville> villes = villeService.getVilles();
        model.addAttribute("villes", villes);
        return "listeVilles";
    }

    @GetMapping("/addPage")
    public String showFormAddVille(Model model) {
        VilleDto ville = new VilleDto();
        model.addAttribute("ville", ville);
        return "addVille";
    }

    @PostMapping("/add")
    public String addVille(@ModelAttribute("ville") VilleDto ville) throws RessourceNotFoundException, RequeteIncorrecteException, RessourceExistanteException {
        villeService.insertVille(VilleMapper.toEntity(ville));
        return "redirect:/villes/liste";
    }

    @GetMapping("/update/{id}")
    public String showFormUpdateVille(@PathVariable long id, Model model) throws RessourceNotFoundException, RequeteIncorrecteException {
        Ville ville = villeService.getVilleById(id);
        model.addAttribute("ville", VilleMapper.toDto(ville));
        return "updateVille";
    }

    @PostMapping("/update/{id}")
    public String updateVille(@PathVariable long id, @ModelAttribute("ville") VilleDto ville) throws RessourceNotFoundException, RequeteIncorrecteException {
        villeService.updateVille(id, VilleMapper.toEntity(ville));
        return "redirect:/villes/liste";
    }

    @GetMapping("/delete/{id}")
    public String deleteVille(@PathVariable long id) throws RessourceNotFoundException, RequeteIncorrecteException {
        villeService.deleteVille(id);
        return "redirect:/villes/liste";
    }
}
